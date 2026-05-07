package ru.cementpromo.service;

import ru.cementpromo.model.Order;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class OrderManagerService {
    private final OrderFileService fileService;
    private final CementPromoService promoService;

    public OrderManagerService(OrderFileService fileService, CementPromoService promoService) {
        this.fileService = fileService;
        this.promoService = promoService;
    }

    public void processOrders(Path inputPath, Path outputPath) {
        List<Order> orders = fileService.readAllOrders(inputPath);
        Map<String, BigDecimal> results = promoService.calculate(orders);
        fileService.writeResults(outputPath, results);
    }
}