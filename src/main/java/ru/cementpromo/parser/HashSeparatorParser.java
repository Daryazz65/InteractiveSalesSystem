package ru.cementpromo.parser;

import ru.cementpromo.model.Order;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class HashSeparatorParser implements OrderParser {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public List<Order> parse(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            return lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> {
                        String[] parts = line.split("#");
                        return new Order(
                                LocalDateTime.parse(parts[0].trim(), DATE_FORMATTER),
                                parts[1].trim(),
                                Integer.parseInt(parts[2].trim())
                        );
                    })
                    .toList();
        }
    }
}

