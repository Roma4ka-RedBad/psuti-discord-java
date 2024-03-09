package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.redbad.DescObject;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.text.ParseException;


public class DescSSGroupsEvent implements Listener<StringSelectInteractionEvent> {
    private final ComponentsPayload payloadManager;

    public DescSSGroupsEvent(ComponentsPayload payloadManager) {
        this.payloadManager = payloadManager;
    }

    public void run(StringSelectInteractionEvent event, Parser parser) {
        MessageConstructor constructor = new MessageConstructor();
        try {
            String groupLink = event.getValues().get(0);
            DescObject group = parser.get_desc_by_group(groupLink);
            DescWeek week = group.get_week();
            if (!week.days.isEmpty()) {
                constructor.buildStringSelect(payloadManager.addPayload("week-list", groupLink), Utils.getWeekdays(week.days), group.groupName+" | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ", false);
                constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
                event.deferReply().flatMap(v -> event.getHook().editOriginal(constructor.buildAsEdit())).queue();
            }
            else
                event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("У данной группы нет пар на этой неделе!")).queue();
        } catch (ParseException ignored) {}
    }
}
