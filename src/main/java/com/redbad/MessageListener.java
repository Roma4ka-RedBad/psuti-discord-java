package com.redbad;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;
import org.redbad.Parser;
import java.util.Map;
import java.util.Objects;


public class MessageListener extends ListenerAdapter {
    public Parser schedule;
    public MessageListener(Parser schedule) {
        this.schedule = schedule;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        schedule.getHTMLModel("");
        if (!schedule.requestBody.isDone) {
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Упс! Кажется сайт накрылся :)")).queue();
            return;
        }

        Map<String, String> groups = schedule.get_groups();
        switch (event.getName()) {
            case "desc":
                if (!Objects.isNull(event.getOption("group"))) {
                    if (groups.containsKey(event.getOption("group").getAsString().toUpperCase())) {
                        event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("OK!")).queue();
                    } else {
                        event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("Группа не найдена!")).queue();
                    }
                } else {
                    event.deferReply(false).flatMap(v -> event.getHook().editOriginal(
                            MessageEditData.fromCreateData(Utils.getStringSelect(Utils.group_parts(groups),
                                    "group-list-%s",
                                    "ВЫБЕРИТЕ ГРУППУ | ЛИСТ %s")))
                    ).queue();
                }
                break;
            case "add_group": break;
            case "del_group": break;
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().startsWith("group-list")) {
            event.reply("You chose " + event.getValues().get(0)).queue();
        }
    }
}
