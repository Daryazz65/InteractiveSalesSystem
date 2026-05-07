package ru.cementpromo.service;
import org.mockito.InOrder;
import ru.cementpromo.model.Order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderManagerServiceTest{

    @Mock
    private OrderFileService fileService;
    @Mock
    private  CementPromoService promoService;
    @InjectMocks
    private OrderManagerService orderManagerService;
    @TempDir
    Path tempDir;

    private Path inputPath;
    private Path outputPath;
    private List<Order> mockOrders;
    private Map<String, BigDecimal> mockResults;

    @BeforeEach
    void setUp(){
        inputPath = tempDir.resolve("input");
        outputPath = tempDir.resolve("output.txt");

        mockOrders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "Company A", 1000),
                new Order(LocalDateTime.of(2023, 10, 1, 11, 0), "Company B", 500)
        );
        mockResults = Map.of(
                "Company A", new BigDecimal("5000.00"),
                "Company b", new BigDecimal("2250.00")
        );
    }

    @Test
    void processOrders_shouldReadCalculateAndWrite(){
        when(fileService.readAllOrders(any(Path.class))).thenReturn(mockOrders);
        when(promoService.calculate(anyList(),any(),any(),any())).thenReturn(mockResults);
        orderManagerService.processOrders(inputPath, outputPath);
        verify(fileService).readAllOrders(inputPath);
        verify(promoService).calculate(eq(mockOrders),any(),any(),any());
        verify(fileService).writeResults(outputPath, mockResults);
    }

    @Test
    void processOrders_shouldHandleEmptyOrders() {
        when(fileService.readAllOrders(any(Path.class))).thenReturn(List.of());
        when(promoService.calculate(anyList(), any(), any(), any())).thenReturn(Map.of());
        orderManagerService.processOrders(inputPath, outputPath);
        verify(fileService).readAllOrders(inputPath);
        verify(promoService).calculate(eq(List.of()), any(), any(), any());
        verify(fileService).writeResults(outputPath, Map.of());
    }

    @Test
    void processOrders_shouldThrowExceptionWhenReadFails() {
        when(fileService.readAllOrders(any(Path.class)))
                .thenThrow(new RuntimeException("File read error"));
        assertThrows(RuntimeException.class, () ->
                orderManagerService.processOrders(inputPath, outputPath)
        );
    }

    @Test
    void processOrders_shouldThrowExceptionWhenCalculateFails() {
        when(fileService.readAllOrders(any(Path.class))).thenReturn(mockOrders);
        when(promoService.calculate(anyList(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid discount calculation"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderManagerService.processOrders(inputPath, outputPath)
        );

        assertEquals("Invalid discount calculation", exception.getMessage());

        verify(fileService, never()).writeResults(any(), any());
    }

    @Test
    void processOrders_shouldPassCorrectParametersToCalculate() {
        when(fileService.readAllOrders(any(Path.class))).thenReturn(mockOrders);
        when(promoService.calculate(anyList(), any(), any(), any())).thenReturn(mockResults);

        orderManagerService.processOrders(inputPath, outputPath);

        verify(promoService, times(1)).calculate(
                eq(mockOrders),
                any(BigDecimal.class),
                any(BigDecimal.class),
                any(BigDecimal.class)
        );

        InOrder inOrder = inOrder(fileService, promoService);
        inOrder.verify(fileService).readAllOrders(any(Path.class));
        inOrder.verify(promoService).calculate(anyList(), any(), any(), any());
    }
}
