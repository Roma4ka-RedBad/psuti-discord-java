package com.redbad.listeners.add_group;

import com.redbad.Database;
import com.redbad.listeners.Listener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.util.Map;
import java.util.Objects;

public class AddGroupListener implements Listener<SlashCommandInteractionEvent> {
    private final Database database;
    public AddGroupListener(Database database) {
        this.database = database;
    }

    public void run(SlashCommandInteractionEvent event, Parser parser) {
        String groupName = event.getOption("group").getAsString().toUpperCase();
        long guildId = Objects.isNull(event.getGuild()) ? 0 : Long.parseLong(event.getGuild().getId());
        long channelId = Long.parseLong(event.getChannel().getId());
        Map<String, String> groups = parser.get_groups();
        DescWeek totalWeek, nextWeek;

        if (groups.containsKey(groupName) && !database.chatAndGroupExists(groupName, guildId, channelId)) {
            try {
                totalWeek = parser.get_desc_by_group(groups.get(groupName)).get_week();
                nextWeek = parser.get_desc_by_group(totalWeek.nextWeekHREF).get_week();
            } catch (Exception err) {
                event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("_Произошла ошибка! Попробуйте позже._")).queue();
                return;
            }

            database.addSqlGroup(groupName, totalWeek, nextWeek, guildId, channelId);
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat(String.format("_Группа **%s** добавлена в лист событий!_", groupName))).queue();
        } else {
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("_Группа не найдена или уже добавлена для этого чата!_")).queue();
        }
    }
}
