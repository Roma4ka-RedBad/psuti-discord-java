package com.redbad;

import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
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

    public static MessageCreateData getStringSelect(List<Map<String, String>> list, String menu_id, String name) {
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
}
