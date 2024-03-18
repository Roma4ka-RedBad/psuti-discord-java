package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.objects.Payload;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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
        event.deferEdit().queue();
        MessageConstructor constructor = new MessageConstructor();
        try {
            String groupLink = event.getValues().get(0);
            DescObject group = parser.get_desc_by_group(groupLink);
            DescWeek week = group.get_week();
            if (!week.days.isEmpty()) {
                constructor.buildStringSelect(payloadManager.addPayload("week-list", groupLink), Utils.getWeekdays(week.days), group.groupName+" | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ", false);
                constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
                constructor.addButtons(
                        Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.lastWeekHREF)), "ПРЕД. НЕДЕЛЯ"),
                        Button.primary(payloadManager.addPayload("choice-group", event.getMember().getIdLong()), "СПИСОК ГРУПП"),
                        Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.nextWeekHREF)), "СЛЕД. НЕДЕЛЯ")
                );
                event.getHook().editOriginal(constructor.buildAsEdit()).queue();
            }
            else
                event.getHook().sendMessage("У данной группы нет пар на этой неделе!").setEphemeral(true).queue();
        } catch (ParseException ignored) {}
    }
}
