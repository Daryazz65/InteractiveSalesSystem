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

        BigDecimal pricePerKg = new BigDecimal("10");
        BigDecimal startDiscount = new BigDecimal("0.50");
        BigDecimal discountStep = new BigDecimal("0.05");

        Map<String, BigDecimal> results = promoService.calculate(
                orders,
                pricePerKg,
                startDiscount,
                discountStep
        );

        fileService.writeResults(outputPath, results);
    }
}