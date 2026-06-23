package ru.cementpromo.parser;

import ru.cementpromo.exception.IORuntimeException;
import ru.cementpromo.model.Order;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class HashSeparatorParser implements OrderParser {
    private static final int TIME_INDEX = 0;
    private static final int COMPANY_INDEX = 1;
    private static final int KG_INDEX = 2;
    private static final String SEPARATOR = "#";

    @Override
    public List<Order> parse(Path file) {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::toOrder)
                    .toList();
        } catch (IOException e) {
            throw new IORuntimeException("Ошибка чтения файла: " + file, e);
        }
    }

    private Order toOrder(String line) {
        String[] parts = line.split(SEPARATOR);
        return new Order(
                LocalDateTime.parse(parts[TIME_INDEX].trim(), DATE_FORMATTER),
                parts[COMPANY_INDEX].trim(),
                Integer.parseInt(parts[KG_INDEX].trim())
        );
    }
}