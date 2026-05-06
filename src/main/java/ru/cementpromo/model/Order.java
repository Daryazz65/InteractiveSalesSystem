package ru.cementpromo.model;

import java.time.LocalDateTime;

public record Order (LocalDateTime time, String company, int kg) {
    // что то тут еще надо?
}
