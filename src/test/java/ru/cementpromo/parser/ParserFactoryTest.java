package ru.cementpromo.parser;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ParserFactoryTest {

    @Test
    void select_txtFile_shouldReturnPipeSeparatorParser() {
        Path path = Path.of("data.txt");
        OrderParser parser = ParserFactory.select(path);
        assertNotNull(parser);
        assertTrue(parser instanceof PipeSeparatorParser);
    }

    @Test
    void select_fileWithoutExtension_shouldReturnHashSeparatorParser() {
        Path path = Path.of("data");
        OrderParser parser = ParserFactory.select(path);
        assertNotNull(parser);
        assertTrue(parser instanceof HashSeparatorParser);
    }

    @Test
    void select_fileWithOtherExtension_shouldReturnHashSeparatorParser() {
        Path csvPath = Path.of("data.csv");
        Path xmlPath = Path.of("data.xml");
        assertTrue(ParserFactory.select(csvPath) instanceof HashSeparatorParser);
        assertTrue(ParserFactory.select(xmlPath) instanceof HashSeparatorParser);
    }

    @Test
    void select_upperCaseTxt_shouldReturnPipeSeparatorParser() {
        Path path = Path.of("data.TXT");
        OrderParser parser = ParserFactory.select(path);
        assertTrue(parser instanceof PipeSeparatorParser);
    }
}