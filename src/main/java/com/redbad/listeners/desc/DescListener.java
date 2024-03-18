package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.objects.Payload;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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
        MessageConstructor constructor = new MessageConstructor();
        event.deferReply(true).queue();
        if (event.getOption("group") != null) {
            Map<String, String> groups = parser.get_groups();
            String groupName = Objects.requireNonNull(event.getOption("group")).getAsString().toUpperCase();
            if (groups.containsKey(groupName)) {
                try {
                    DescWeek week = parser.get_desc_by_group(groups.get(groupName)).get_week();
                    if (!week.days.isEmpty()) {
                        constructor.buildStringSelect(payloadManager.addPayload("week-list", groups.get(groupName)), Utils.getWeekdays(week.days), groupName+" | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ", false);
                        constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
                        constructor.addButtons(
                                Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.lastWeekHREF)), "ПРЕД. НЕДЕЛЯ"),
                                Button.primary(payloadManager.addPayload("choice-group", event.getMember().getIdLong()), "СПИСОК ГРУПП"),
                                Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.nextWeekHREF)), "СЛЕД. НЕДЕЛЯ")
                        );
                        event.getHook().editOriginal(constructor.buildAsEdit()).queue();
                    }
                    else
                        event.getHook().editOriginalFormat("У данной группы нет пар на этой неделе!").queue();
                    } catch (ParseException ignored) {}
            } else {
                event.getHook().editOriginalFormat("Группа не найдена!").queue();
            }
        } else {
            Map<Integer, Map<String, String>> groups = parser.get_groups_course();
            Map<Integer, List<Map<String, String>>> dividedGroups = Utils.divideCoursesMaps(groups);
            dividedGroups.forEach((course, item) -> item.forEach(map -> constructor.buildStringSelect(String.format("group-list-%s-%s", course, item.indexOf(map)), map, "ВЫБЕРИТЕ ГРУППУ | КУРС " + course, true)));
            event.getHook().editOriginal(constructor.buildAsEdit()).queue();
        }
    }
}
