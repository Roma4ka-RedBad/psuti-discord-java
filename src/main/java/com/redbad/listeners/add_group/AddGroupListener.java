package com.redbad.listeners.add_group;

import com.redbad.Database;
import com.redbad.objects.Listener;
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
        event.deferReply(true).queue();
        String groupName = Objects.requireNonNull(event.getOption("group")).getAsString().toUpperCase();
        long guildId = (event.getGuild() == null) ? 0 : event.getGuild().getIdLong();
        long channelId = Long.parseLong(event.getChannel().getId());
        Map<String, String> groups = parser.get_groups();
        DescWeek totalWeek, nextWeek;

        if (groups.containsKey(groupName) && !database.chatAndGroupExists(groupName, guildId, channelId)) {
            try {
                totalWeek = parser.get_desc_by_group(groups.get(groupName)).get_week();
                nextWeek = parser.get_desc_by_group(totalWeek.nextWeekHREF).get_week();
            } catch (Exception err) {
                event.getHook().editOriginalFormat("_Произошла ошибка! Попробуйте позже._").queue();
                return;
            }

            database.addSqlGroup(groupName, totalWeek, nextWeek, guildId, channelId);
            event.getHook().editOriginalFormat(String.format("_Группа **%s** добавлена в лист событий!_", groupName)).queue();
        } else {
            event.getHook().editOriginalFormat("_Группа не найдена или уже добавлена для этого чата!_").queue();
        }
    }
}
