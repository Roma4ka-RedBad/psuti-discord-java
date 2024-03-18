package com.redbad.listeners.del_group;

import com.redbad.Database;
import com.redbad.objects.Listener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.redbad.Parser;

import java.util.Objects;


public class DelGroupListener implements Listener<SlashCommandInteractionEvent> {
    private final Database database;

    public DelGroupListener(Database database) {
        this.database = database;
    }

    public void run(SlashCommandInteractionEvent event, Parser parser) {
        event.deferReply(true).queue();
        String groupName = Objects.requireNonNull(event.getOption("group")).getAsString().toUpperCase();
        long guildId = (event.getGuild() == null) ? 0 : event.getGuild().getIdLong();
        long channelId = Long.parseLong(event.getChannel().getId());

        if (database.chatAndGroupExists(groupName, guildId, channelId)) {
            database.delGroup(groupName, guildId, channelId);
            event.getHook().editOriginalFormat(String.format("_Группа **%s** удалена из листа событий!_", groupName)).queue();
        } else {
            event.getHook().editOriginalFormat("_Группа не найдена или уже удалена для этого чата!_").queue();
        }
    }
}
