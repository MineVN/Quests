package me.manaki.plugin.quests.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.LocalDate;

public class Utils {

    public static Location toLocation(String s) {
        String[] a = s.split(";");
        var w = Bukkit.getWorld(a[0]);
        var x = Double.parseDouble(a[1]);
        var y = Double.parseDouble(a[2]);
        var z = Double.parseDouble(a[3]);
        return new Location(w, x, y, z);
    }

    public static String toString(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

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
