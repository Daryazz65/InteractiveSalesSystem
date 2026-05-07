package ru.cementpromo;

import ru.cementpromo.model.Order;
import ru.cementpromo.parser.ParserFactory;
import ru.cementpromo.service.CementPromoService;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Path inputDir = Paths.get("orders");
        Path outputFile =Paths.get("result.txt");

        try {
            try (Stream<Path> fileStream = Files.list(inputDir)) {

                List<Order> allOrders =fileStream
                        .filter(Files::isRegularFile)
                        .flatMap(path -> {
                            try {
                                return ParserFactory.select(path).parse(path).stream();
                            } catch (IOException e) {
                                throw new RuntimeException("Ошибка чтения файла: " + path, e);
                            }
                        })
                        .collect(Collectors.toList());

                CementPromoService service = new CementPromoService();
                Map<String, BigDecimal> result = service.calculate(allOrders);

                List<String> linesToWrite = result.entrySet().stream()
                        .map(entry -> entry.getKey() + " - " + entry.getValue())
                        .toList();

                Files.write(outputFile, linesToWrite);

                System.out.println("Результат сохранен в " + outputFile.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println(" Ошибка: " + e.getMessage());
        }
    }
}