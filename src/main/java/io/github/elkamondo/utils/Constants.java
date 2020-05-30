package io.github.elkamondo.utils;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Constants {

    private Constants() {}

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = ofPattern("dd/MM/yyyy HH:mm:ss");

}
