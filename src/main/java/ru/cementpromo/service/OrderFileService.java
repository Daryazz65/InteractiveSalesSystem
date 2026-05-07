package ru.cementpromo.service;

import ru.cementpromo.model.Order;
import ru.cementpromo.parser.IORuntimeException;
import ru.cementpromo.parser.ParserFactory;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderFileService {

    public List<Order> readAllOrders(Path inputDir) {
        try (Stream<Path> fileStream = Files.list(inputDir)) {
            return fileStream
                    .filter(Files::isRegularFile)
                    .flatMap(path -> {
                        try {
                            return ParserFactory.select(path).parse(path).stream();
                        } catch (IORuntimeException e) {
                            throw e;
                        } catch (Exception e) {
                            throw new IORuntimeException("Ошибка при обработке файла: " + path, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (java.io.IOException e) {
            throw new IORuntimeException("Ошибка чтения директории: " + inputDir, e);
        }
    }

    public void writeResults(Path outputFile, Map<String, BigDecimal> results) {
        List<String> lines = results.entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .toList();

        try {
            Files.write(outputFile, lines);
        } catch (java.io.IOException e) {
            throw new IORuntimeException("Ошибка записи в файл: " + outputFile, e);
        }
    }
}