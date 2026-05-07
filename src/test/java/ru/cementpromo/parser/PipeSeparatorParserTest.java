package ru.cementpromo.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.cementpromo.model.Order;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PipeSeparatorParserTest {
    @TempDir
    Path tempDir;
    private final PipeSeparatorParser parser = new PipeSeparatorParser();

    @Test
    void parse_validFile_shouldParseCorrectly() throws IOException {
        Path file = tempDir.resolve("valid.txt");
        String content =
                "2023-10-01T10:00:00|CompanyA|1000\n" +
                        "2023-10-01T11:00:00|CompanyB|500";
        Files.writeString(file, content);
        List<Order> orders = parser.parse(file);
        assertEquals(2, orders.size());

        Order order1 = orders.get(0);
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0, 0), order1.time());
        assertEquals("CompanyA", order1.company());
        assertEquals(1000, order1.kg());

        Order order2 = orders.get(1);
        assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0, 0), order2.time());
        assertEquals("CompanyB", order2.company());
        assertEquals(500, order2.kg());
    }

    @Test
    void parse_fileWithEmptyLines_shouldSkipEmptyLines() throws IOException {
        Path file = tempDir.resolve("empty_lines.txt");
        String content =
                "2023-10-01T10:00:00|CompanyA|1000\n" +
                        "\n" +
                        "2023-10-01T11:00:00|CompanyB|500\n" +
                        "\n";

        Files.writeString(file, content);
        List<Order> orders = parser.parse(file);
        assertEquals(2, orders.size());
    }

    @Test
    void parse_linesWithSpaces_shouldTrimCorrectly() throws IOException {
        Path file = tempDir.resolve("spaces.txt");
        String content = "  2023-10-01T10:00:00  |  CompanyA  |  1000  ";

        Files.writeString(file, content);
        List<Order> orders = parser.parse(file);
        assertEquals(1, orders.size());
        Order order = orders.get(0);

        assertEquals("CompanyA", order.company());
        assertEquals(1000, order.kg());
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0, 0), order.time());
    }

    @Test
    void parse_invalidDateFormat_shouldThrowException() throws IOException {
        Path file = tempDir.resolve("bad_date.txt");
        String content = "01.10.2023 10:00|CompanyA|1000";

        Files.writeString(file, content);
        assertThrows(RuntimeException.class, () -> parser.parse(file));
    }

    @Test
    void parse_invalidWeight_shouldThrowException() throws IOException{
        Path file = tempDir.resolve("bad_weight.txt");
        String content = "2023-10-01T10:00:00|CompanyA|abc";

        Files.writeString(file, content);
        assertThrows(RuntimeException.class, () -> parser.parse(file));
    }

    @Test
    void parse_wrongNumberOfFields_shouldThrowException() throws IOException {
        Path file = tempDir.resolve("bad_fields.txt");
        String content = "2023-10-01T10:00:00|CompanyA";

        Files.writeString(file, content);
        assertThrows(RuntimeException.class, () -> parser.parse(file));
    }

    @Test
    void parse_emptyFile_shouldReturnEmptyList() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");

        List<Order> orders = parser.parse(file);
        assertTrue(orders.isEmpty());
    }
}