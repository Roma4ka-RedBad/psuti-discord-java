package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.objects.Payload;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.redbad.DescObject;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.text.ParseException;


public class DescBtnSwapEvent implements Listener<ButtonInteractionEvent> {
    private final ComponentsPayload payloadManager;

    public DescBtnSwapEvent(ComponentsPayload payloadManager) {
        this.payloadManager = payloadManager;
    }

    public void run(ButtonInteractionEvent event, Parser parser) {
        event.deferEdit().queue();
        Payload payload = (Payload) payloadManager.getPayload(event.getComponentId());

        if (payload == null) {
            event.getHook().sendMessage("Вызовите сообщение ещё раз!").setEphemeral(true).queue();
            return;
        }

        if (event.getMember().getIdLong() == (Long) payload.get("memberId")) {
            try {
                String groupLink = (String) payload.get("groupLink");
                DescObject group = parser.get_desc_by_group(groupLink);
                DescWeek week = group.get_week();
                MessageConstructor constructor = new MessageConstructor();
                if (!week.days.isEmpty())
                    constructor.buildStringSelect(payloadManager.addPayload("week-list", groupLink), Utils.getWeekdays(week.days), group.groupName + " | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ", false);
                constructor.addContent(String.format("%s - %s", Utils.datePattern.format(week.startDate), Utils.datePattern.format(week.endDate)));
                constructor.addButtons(
                        Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.lastWeekHREF)), "ПРЕД. НЕДЕЛЯ"),
                        Button.primary(payloadManager.addPayload("choice-group", event.getMember().getIdLong()), "СПИСОК ГРУПП"),
                        Button.success(payloadManager.addPayload("swap-week", new Payload().put("memberId", event.getMember().getIdLong()).put("groupLink", week.nextWeekHREF)), "СЛЕД. НЕДЕЛЯ")
                );
                event.getHook().editOriginal(constructor.buildAsEdit()).queue();
            } catch (ParseException ignored) {}
        } else {
            event.getHook().sendMessage("Вам не принадлежит это сообщение! Вызовите своё в этом чате или у меня в личных сообщениях!").setEphemeral(true).queue();
        }
    }
}
