package me.manaki.plugin.quests.utils;

import java.time.LocalDate;

public class Utils {

    public static String toString(LocalDate date) {
        return date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();
    }

    public static LocalDate parse(String s) {
        int day = Integer.parseInt(s.split("/")[0]);
        int month = Integer.parseInt(s.split("/")[1]);
        int year = Integer.parseInt(s.split("/")[2]);
        return LocalDate.of(year, month, day);
    }

}
