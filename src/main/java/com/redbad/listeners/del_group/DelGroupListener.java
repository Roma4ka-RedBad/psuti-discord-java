package com.redbad.listeners.del_group;

import com.redbad.Database;
import com.redbad.listeners.Listener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.redbad.Parser;

import java.util.Objects;

public class DelGroupListener implements Listener<SlashCommandInteractionEvent> {
    private final Database database;
    public DelGroupListener(Database database) {
        this.database = database;
    }
    public void run(SlashCommandInteractionEvent event, Parser parser) {
        String groupName = event.getOption("group").getAsString().toUpperCase();
        long guildId = Objects.isNull(event.getGuild()) ? 0 : Long.parseLong(event.getGuild().getId());
        long channelId = Long.parseLong(event.getChannel().getId());

        if (database.chatAndGroupExists(groupName, guildId, channelId)) {
            database.delGroup(groupName, guildId, channelId);
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat(String.format("_Группа **%s** удалена из листа событий!_", groupName))).queue();
        } else {
            event.deferReply(true).flatMap(v -> event.getHook().editOriginalFormat("_Группа не найдена или уже удалена для этого чата!_")).queue();
        }
    }
}
