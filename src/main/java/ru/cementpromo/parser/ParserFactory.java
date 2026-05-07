package ru.cementpromo.parser;

import java.nio.file.Path;

public class ParserFactory {
    public static OrderParser select(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();

        if(fileName.endsWith(".txt")) {
            return new PipeSeparatorParser();
        }

        return new HashSeparatorParser();
    }
}