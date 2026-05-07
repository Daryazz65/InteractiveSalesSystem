package ru.cementpromo;

import ru.cementpromo.service.ApplicationService;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Path inputDir = Paths.get("orders");
        Path outputFile = Paths.get("result.txt");

        BigDecimal pricePerKg = new BigDecimal("10");
        BigDecimal startDiscount = new BigDecimal("0.50");
        BigDecimal discountStep = new BigDecimal("0.05");

        ApplicationService appService = new ApplicationService(
                pricePerKg,
                startDiscount,
                discountStep
        );

        appService.processOrders(inputDir, outputFile);

        System.out.println("Результат сохранен в " + outputFile.toAbsolutePath());
    }
}