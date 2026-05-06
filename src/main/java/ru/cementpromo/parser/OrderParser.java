package ru.cementpromo.parser;

import ru.cementpromo.model.Order;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface OrderParser {
    List<Order> parse(Path file) throws IOException;
}


