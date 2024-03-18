package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.objects.Payload;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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
        event.deferEdit().queue();
        MessageConstructor constructor = new MessageConstructor();
        try {
            String groupLink;
            Object payload = payloadManager.getPayload(event.getComponentId());

            if (payload == null) {
                event.getHook().sendMessage("Вызовите сообщение ещё раз!").setEphemeral(true).queue();
                return;
            } else {
                groupLink = (String) payload;
            }

            DescWeek week = parser.get_desc_by_group(groupLink).get_week();
            DescDay day = week.days.get(Integer.parseInt(event.getValues().get(0)));
            day.lessons.forEach(lesson -> constructor.buildEmbedByLesson(day, lesson, Parser.BASE_URL, groupLink));
            constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
            constructor.addButtons(
                    Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.lastWeekHREF)), "ПРЕД. НЕДЕЛЯ"),
                    Button.primary(payloadManager.addPayload("choice-group", event.getMember().getIdLong()), "СПИСОК ГРУПП"),
                    Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.nextWeekHREF)), "СЛЕД. НЕДЕЛЯ")
            );
            event.getHook().editOriginal(constructor.buildAsEdit()).queue();
        } catch (ParseException ignored) {}
    }
}
