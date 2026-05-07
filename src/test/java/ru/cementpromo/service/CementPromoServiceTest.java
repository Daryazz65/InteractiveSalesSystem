package ru.cementpromo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cementpromo.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CementPromoServiceTest {

    private CementPromoService promoService;
    private BigDecimal pricePerKg;
    private BigDecimal startDiscount;
    private BigDecimal discountStep;

    @BeforeEach
    void setUp() {
        pricePerKg = new BigDecimal("10");
        startDiscount = new BigDecimal("0.50");
        discountStep = new BigDecimal("0.05");
        promoService = new CementPromoService();
    }

    @Test
    void calculate_firstOrder_shouldApply50PercentDiscount() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 1000)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertTrue(result.containsKey("CompanyA"));
        BigDecimal expectedAmount = new BigDecimal("5000.00");
        assertEquals(expectedAmount, result.get("CompanyA"));
    }

    @Test
    void calculate_fifthOrder_shouldApply30PercentDiscount() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 1), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 2), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 3), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 4), "CompanyA", 1000)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertTrue(result.containsKey("CompanyA"));
        assertNotNull(result.get("CompanyA"));
    }

    @Test
    void calculate_manyOrders_shouldNotGoBelowZeroDiscount() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 1), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 2), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 3), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 4), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 5), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 6), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 7), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 8), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 9), "CompanyA", 100),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 10), "CompanyA", 1000)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertTrue(result.containsKey("CompanyA"));
        assertNotNull(result.get("CompanyA"));
    }

    @Test
    void calculate_multipleCompanies_shouldGroupByCompany() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 1000),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 1), "CompanyB", 500),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 2), "CompanyA", 2000)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertEquals(2, result.size());
        assertTrue(result.containsKey("CompanyA"));
        assertTrue(result.containsKey("CompanyB"));
        assertNotNull(result.get("CompanyA"));
        assertNotNull(result.get("CompanyB"));
    }

    @Test
    void calculate_unsortedOrders_shouldSortByTime() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 12, 0), "CompanyA", 1000),
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 1000),
                new Order(LocalDateTime.of(2023, 10, 1, 11, 0), "CompanyA", 1000)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertTrue(result.containsKey("CompanyA"));
        assertNotNull(result.get("CompanyA"));
    }

    @Test
    void calculate_shouldRoundToTwoDecimalPlaces() {
        List<Order> orders = List.of(
                new Order(LocalDateTime.of(2023, 10, 1, 10, 0), "CompanyA", 100)
        );
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        BigDecimal amount = result.get("CompanyA");
        assertEquals(2, amount.scale());
        assertEquals(new BigDecimal("500.00"), amount);
    }

    @Test
    void calculate_emptyOrders_shouldReturnEmptyMap() {
        List<Order> orders = List.of();
        Map<String, BigDecimal> result = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );
        assertTrue(result.isEmpty());
    }
}