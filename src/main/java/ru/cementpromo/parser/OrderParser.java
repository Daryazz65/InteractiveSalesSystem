package ru.cementpromo.parser;

import ru.cementpromo.model.Order;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrderParser {
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    List<Order> parse(Path file);
}