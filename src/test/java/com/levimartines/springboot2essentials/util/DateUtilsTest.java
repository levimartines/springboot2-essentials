package com.levimartines.springboot2essentials.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    private final DateUtils utils = new DateUtils();

    @Test
    void formatLocalDateTimeToDbStyle() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
        String formattedDateTime = utils.formatLocalDateTimeToDbStyle(dateTime);

        assertEquals("2021-01-01 01:01:01", formattedDateTime);
    }
}
