package com.ewmservice.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final LocalDateTime MAX_DATE = LocalDateTime.now().plusYears(5);
}
