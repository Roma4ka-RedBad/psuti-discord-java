package com.redbad;

import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.redbad.DescDay;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
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

        // Обработка результатов ResultSet
        while (resultSet.next()) {
            Map<String, Object> resultMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                resultMap.put(columnName, value);
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    public static List<Map<String, String>> group_parts(Map<String, String> map) {
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

    public static MessageCreateData getStringSelectByMaps(List<Map<String, String>> list, String menu_id, String name) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        int index = 0;
        for (Map<String, String> list_element : list) {
            StringSelectMenu.Builder menu = StringSelectMenu.create(String.format(menu_id, index));
            menu.setPlaceholder(String.format(name, index + 1));
            for (Map.Entry<String, String> map_element : list_element.entrySet()) {
                menu.addOption(map_element.getKey().toUpperCase(), map_element.getValue());
            }
            builder.addActionRow(menu.build());
            index += 1;
        }
        return builder.build();
    }

    public static MessageCreateData getStringSelectByList(List<String> list, String menu_id, String name) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        StringSelectMenu.Builder menu = StringSelectMenu.create(menu_id);
        menu.setPlaceholder(name);
        for (String list_element : list) {
            menu.addOption(list_element, Integer.toString(list.indexOf(list_element)));
        }
        builder.addActionRow(menu.build());
        return builder.build();
    }

    public static String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static ArrayList<String> getWeekdays(ArrayList<DescDay> days) {
        SimpleDateFormat weekdayPattern = new SimpleDateFormat("EEEE");
        ArrayList<String> weekdays = new ArrayList<>();
        for (DescDay day : days) {
            weekdays.add(firstUpperCase(weekdayPattern.format(day.date)));
        }
        return weekdays;
    }
}
