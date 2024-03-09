package com.redbad.utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.redbad.DescDay;
import org.redbad.Parser;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utils {
    public static SimpleDateFormat weekdayPattern = new SimpleDateFormat("EEEE");
    public static SimpleDateFormat datePattern = new SimpleDateFormat("dd.MM.yyyy");

    public static boolean checkSiteConnection(Parser parser, SlashCommandInteractionEvent event) {
        parser.getHTMLModel("");
        if (!parser.requestBody.isDone) {
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Упс! Кажется сайт накрылся :)")).queue();
            return true;
        }
        return false;
    }

    public static boolean checkSiteConnection(Parser parser, StringSelectInteractionEvent event) {
        parser.getHTMLModel("");
        if (!parser.requestBody.isDone) {
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Упс! Кажется сайт накрылся :)")).queue();
            return true;
        }
        return false;
    }

    public static StringBuilder[] buildSqliteGroups(Map<String, Object> map) {
        StringBuilder fields = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        String lastKey = null;
        for (String key : map.keySet()) {
            lastKey = key;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            fields.append("'").append(key).append("'");
            if (value instanceof String) {
                values.append("'").append(value).append("'");
            } else {
                values.append(value);
            }

            if (!key.contains(lastKey)) {
                fields.append(", ");
                values.append(", ");
            }

        }
        fields.append(")");
        values.append(")");
        return new StringBuilder[]{fields, values};
    }

    public static List<Map<String, Object>> parseResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Map<String, Object>> resultList = new ArrayList<>();

        while (resultSet.next()) {
            Map<String, Object> resultMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                if (value instanceof Integer)
                    value = ((Integer) value).longValue();
                resultMap.put(columnName, value);
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    public static List<Map<String, String>> divideMap(Map<String, String> map) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(map.entrySet());
        List<Map<String, String>> parts = new ArrayList<>();

        for (int i = 0; i < entryList.size(); i += 25) {
            List<Map.Entry<String, String>> subList = entryList.subList(i, Math.min(i + 25, entryList.size()));
            Map<String, String> subMap = new HashMap<>();
            for (Map.Entry<String, String> entry : subList) {
                subMap.put(entry.getKey(), entry.getValue());
            }
            parts.add(subMap);
        }
        return parts;
    }

    public static boolean containsArray(List<Long[]> list, Long[] targetArray) {
        for (Long[] array : list) {
            if (array.length == targetArray.length) {
                boolean isEqual = true;
                for (int i = 0; i < array.length; i++) {
                    if (!array[i].equals(targetArray[i])) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static ArrayList<String> getWeekdays(ArrayList<DescDay> days) {
        ArrayList<String> weekdays = new ArrayList<>();
        for (DescDay day : days) {
            weekdays.add(firstUpperCase(weekdayPattern.format(day.date)));
        }
        return weekdays;
    }
}
