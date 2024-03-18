package com.redbad.listeners.desc;

import com.redbad.objects.Listener;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.MessageConstructor;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.redbad.Parser;

import java.util.List;
import java.util.Map;


public class DescBtnChoiceEvent implements Listener<ButtonInteractionEvent> {
    private final ComponentsPayload payloadManager;

    public DescBtnChoiceEvent(ComponentsPayload payloadManager) {
        this.payloadManager = payloadManager;
    }

    public void run(ButtonInteractionEvent event, Parser parser) {
        event.deferEdit().queue();
        long memberId;
        Object payload = payloadManager.getPayload(event.getComponentId());

        if (payload == null) {
            event.getHook().sendMessage("Вызовите сообщение ещё раз!").setEphemeral(true).queue();
            return;
        } else {
            memberId = (Long) payload;
        }

        if (event.getMember().getIdLong() == memberId) {
            MessageConstructor constructor = new MessageConstructor();
            Map<Integer, Map<String, String>> groups = parser.get_groups_course();
            Map<Integer, List<Map<String, String>>> dividedGroups = Utils.divideCoursesMaps(groups);
            dividedGroups.forEach((course, item) -> item.forEach(map -> constructor.buildStringSelect(String.format("group-list-%s-%s", course, item.indexOf(map)), map, "ВЫБЕРИТЕ ГРУППУ | КУРС " + course, true)));
            event.getHook().editOriginal(constructor.buildAsEdit()).queue();
        } else {
            event.getHook().sendMessage("Вам не принадлежит это сообщение! Вызовите своё в этом чате или у меня в личных сообщениях!").setEphemeral(true).queue();
        }
    }

}
