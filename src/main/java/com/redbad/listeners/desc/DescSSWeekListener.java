package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.redbad.DescDay;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.text.ParseException;


public class DescSSWeekListener implements Listener<StringSelectInteractionEvent> {
    private final ComponentsPayload payloadManager;

    public DescSSWeekListener(ComponentsPayload payloadManager) {
        this.payloadManager = payloadManager;
    }

    public void run(StringSelectInteractionEvent event, Parser parser) {
        MessageConstructor constructor = new MessageConstructor();
        try {
            String groupLink = (String) payloadManager.getPayload(event.getComponentId(), true);
            DescWeek week = parser.get_desc_by_group(groupLink).get_week();
            DescDay day = week.days.get(Integer.parseInt(event.getValues().get(0)));
            day.lessons.forEach(lesson -> constructor.buildEmbedByLesson(day, lesson, parser.baseUrl, groupLink));
            constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
            event.deferEdit().flatMap(v -> event.getHook().editOriginal(constructor.buildAsEdit())).queue();
        } catch (ParseException ignored) {}
    }
}
