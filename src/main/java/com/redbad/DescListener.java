package com.redbad;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;
import org.redbad.DescDay;
import org.redbad.DescObject;
import org.redbad.Parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class DescListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("desc")) {
            Parser schedule = new Parser();
            schedule.getHTMLModel("");
            if (!schedule.requestBody.isDone) {
                event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Упс! Кажется сайт накрылся :)")).queue();
                return;
            }

            Map<String, String> groups = schedule.get_groups();
            if (!Objects.isNull(event.getOption("group"))) {
                if (groups.containsKey(event.getOption("group").getAsString().toUpperCase())) {
                    event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("OK!")).queue();
                } else {
                    event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Группа не найдена!")).queue();
                }
            } else {
                event.deferReply(false).flatMap(v -> event.getHook().editOriginal(
                        MessageEditData.fromCreateData(Utils.getStringSelectByMaps(Utils.group_parts(groups),
                                "group-list-%s",
                                "ВЫБЕРИТЕ ГРУППУ | ЛИСТ %s")))
                ).queue();
            }
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (event.getComponentId().startsWith("group-list") || event.getComponentId().startsWith("week-list")) {

            Parser schedule = new Parser();
            schedule.getHTMLModel("");
            if (!schedule.requestBody.isDone) {
                event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Упс! Кажется сайт накрылся :)")).queue();
                return;
            }

            if (event.getComponentId().startsWith("group-list")) {
                String groupLink = event.getValues().get(0);
                DescObject group = schedule.get_desc_by_group(groupLink);
                try {
                    ArrayList<DescDay> days = group.get_week().get_days();
                    event.deferEdit().flatMap(v -> event.getHook().editOriginal(
                            MessageEditData.fromCreateData(Utils.getStringSelectByList(Utils.getWeekdays(days), "week-list-"+groupLink, group.groupName+" | ВЫБЕРИТЕ ДЕНЬ НЕДЕЛИ")))
                    ).queue();
                } catch (ParseException ignored) {}
            } else if (event.getComponentId().startsWith("week-list")) {
                String groupLink = event.getComponentId().split("-")[2];
            }
        }
    }
}
