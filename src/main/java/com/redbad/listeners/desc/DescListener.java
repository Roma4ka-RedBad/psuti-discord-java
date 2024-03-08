package com.redbad.listeners.desc;

import com.redbad.listeners.Listener;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.redbad.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DescListener implements Listener<SlashCommandInteractionEvent> {
    private final ComponentsPayload payloadManager;
    public DescListener(ComponentsPayload payloadManager) {
        this.payloadManager = payloadManager;
    }

    public void run(SlashCommandInteractionEvent event, Parser parser) {
        Map<String, String> groups = parser.get_groups();
        MessageConstructor constructor = new MessageConstructor();
        if (!Objects.isNull(event.getOption("group"))) {
            String groupName = event.getOption("group").getAsString().toUpperCase();
            if (groups.containsKey(groupName)) {
                try {
                    DescWeek week = parser.get_desc_by_group(groups.get(groupName)).get_week();
                    if (!week.days.isEmpty()) {
                        constructor.buildStringSelect(payloadManager.addPayload("week-list", groups.get(groupName)), Utils.getWeekdays(week.days), groupName+" | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ", false);
                        constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
                        event.deferReply().flatMap(v -> event.getHook().editOriginal(constructor.buildAsEdit())).queue();
                    }
                    else
                        event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("У данной группы нет пар на этой неделе!")).queue();
                    } catch (ParseException ignored) {}
            } else {
                event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Группа не найдена!")).queue();
            }
        } else {
            List<Map<String, String>> dividedGroups = Utils.divideMap(groups);
            dividedGroups.forEach(item -> {
                int index = dividedGroups.indexOf(item);
                    constructor.buildStringSelect("group-list-" + index, item, "ВЫБЕРИТЕ ГРУППУ | ЛИСТ " + (index+1), true);
            });
            event.deferReply(false).flatMap(v -> event.getHook().editOriginal(constructor.buildAsEdit())).queue();
        }
    }
}
