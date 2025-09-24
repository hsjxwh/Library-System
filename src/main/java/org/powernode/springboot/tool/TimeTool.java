package org.powernode.springboot.tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeTool {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static String formatTime(LocalDateTime time) {
        return time.format(formatter);
    }
    public static int daysBetween(LocalDateTime start, LocalDateTime end) {
        return (int)ChronoUnit.DAYS.between(start,end);
    }
    public static boolean needDeductDeposit(LocalDateTime start, LocalDateTime end) {
        return daysBetween(start, end) > 0;
    }
}
