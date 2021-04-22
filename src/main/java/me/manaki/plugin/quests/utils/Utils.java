package me.manaki.plugin.quests.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    public static String bold(String s, String prefix) {
        String regex = "(?<bold>\\*[^\\*]+\\*)";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(s);
        while (matcher.find()) {
            String in = matcher.group();
            String n = toSmallCaps(in.replace("*", ""));
            s = s.replace(in, n);
        }

        return s;
    }

    public static List<String> toList(String s, int length, String start) {
        s = bold(s, start);
        List<String> result = Lists.newArrayList();
        if (s == null) {
            return result;
        } else if (!s.contains(" ")) {
            result.add(start + s);
            return result;
        } else {
            String[] words = s.split(" ");
            int l = 0;
            String line = "";

            int i;
            for(i = 0; i < words.length; ++i) {
                l += words[i].length();
                if (l > length) {
                    result.add(line.substring(0, line.length() - 1));
                    l = words[i].length();
                    line = "";
                    line = line + words[i] + " ";
                } else {
                    line = line + words[i] + " ";
                }
            }

            if (!line.equalsIgnoreCase(" ")) {
                result.add(line);
            }

            for(i = 0; i < result.size(); ++i) {
                result.set(i, start + (String)result.get(i));
            }

            return result;
        }
    }

    private static final String[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".split("");
    private static final String[] SMALL_CAPS_ALPHABET = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴩqʀꜱᴛᴜᴠᴡxyᴢ".split("");

    private static String toSmallCaps(String text)
    {
        text = text.toLowerCase();
        StringBuilder convertedBuilder = new StringBuilder();
        for (char textCharacter : text.toCharArray())
        {
            int index = 0;
            boolean successfullyTranslated = false;
            for (String alphabetLetter : ALPHABET)
            {
                if ((textCharacter + "").equals(alphabetLetter))
                {
                    convertedBuilder.append(SMALL_CAPS_ALPHABET[index]);
                    successfullyTranslated = true;
                    break;
                }

                index++;
            }

            if (!successfullyTranslated)
            {
                convertedBuilder.append(textCharacter);
            }
        }

        return convertedBuilder.toString();
    }

}
