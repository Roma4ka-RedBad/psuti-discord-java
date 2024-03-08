package com.redbad.listeners;

import com.redbad.utils.ListenerFactory;
import com.redbad.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redbad.Parser;

import java.util.Objects;

public class ListenerReader extends ListenerAdapter {
    private final ListenerFactory factory;
    public ListenerReader(ListenerFactory factory) {
        this.factory = factory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Parser parser = new Parser();
        if (Utils.checkSiteConnection(parser, event)) {
            return;
        }

        Listener<SlashCommandInteractionEvent> listener = (Listener<SlashCommandInteractionEvent>) factory.getListenerStartsWith(SlashCommandInteractionEvent.class, event.getName());
        if (Objects.nonNull(listener) )
            listener.run(event, parser);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        Parser parser = new Parser();
        if (Utils.checkSiteConnection(parser, event)) {
            return;
        }

        Listener<StringSelectInteractionEvent> listener = (Listener<StringSelectInteractionEvent>) factory.getListenerStartsWith(StringSelectInteractionEvent.class, event.getComponentId());
        if (Objects.nonNull(listener) )
            listener.run(event, parser);
    }
}
