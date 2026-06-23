package ru.cementpromo;

import ru.cementpromo.service.CementPromoService;
import ru.cementpromo.service.OrderFileService;
import ru.cementpromo.service.OrderManagerService;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path inputDir = Paths.get("orders");
        Path outputFile = Paths.get("result.txt");

        OrderFileService fileService = new OrderFileService();
        CementPromoService promoService = new CementPromoService(); // Без параметров!

        OrderManagerService manager = new OrderManagerService(fileService, promoService);

        manager.processOrders(inputDir, outputFile);
        System.out.println("Результат сохранен в " + outputFile.toAbsolutePath());
    }
}
