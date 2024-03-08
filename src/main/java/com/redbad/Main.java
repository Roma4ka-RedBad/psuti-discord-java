package com.redbad;

import com.redbad.listeners.*;
import com.redbad.listeners.add_group.AddGroupListener;
import com.redbad.listeners.del_group.DelGroupListener;
import com.redbad.listeners.desc.DescListener;
import com.redbad.listeners.desc.DescSSGroupsEvent;
import com.redbad.listeners.desc.DescSSWeekListener;
import com.redbad.utils.ComponentsPayload;
import com.redbad.utils.ListenerFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDABuilder builder = JDABuilder.createDefault("MTA4MDgwMDkyNzc1MTA4MjAwNA.GE2NJS.wxHlNU2A5lLJljhTchAvmk9iJJjr2egpfDuJzQ");
        Database database = new Database("database.sqlite");
        ComponentsPayload payloadManager = new ComponentsPayload();
        ListenerFactory factory = new ListenerFactory();
        if (!database.preloaded) {
            System.out.println("Невозможно запуститься! Сайт не работает или произошла ошибка.");
            return;
        }

        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        factory.addEvents(SlashCommandInteractionEvent.class, StringSelectInteractionEvent.class);
        factory.addListener(SlashCommandInteractionEvent.class, "add_group", new AddGroupListener(database));
        factory.addListener(SlashCommandInteractionEvent.class, "del_group", new DelGroupListener(database));
        factory.addListener(SlashCommandInteractionEvent.class, "desc", new DescListener(payloadManager));
        factory.addListener(StringSelectInteractionEvent.class, "group-list", new DescSSGroupsEvent(payloadManager));
        factory.addListener(StringSelectInteractionEvent.class, "week-list", new DescSSWeekListener(payloadManager));
        builder.addEventListeners(new ListenerReader(factory));
        JDA bot = builder.build();

        bot.updateCommands().addCommands(
                Commands.slash("desc", "Команда для просмотра расписания группы")
                        .addOption(OptionType.STRING, "group", "Название конкретной группы для просмотра расписания"),
                Commands.slash("add_group", "Данная команда запустит отслеживание расписания для данного чата")
                        .addOption(OptionType.STRING, "group", "Название нужной группы для запуска отслеживания", true),
                Commands.slash("del_group", "Данная команда остановит отслеживание расписания для данного чата")
                        .addOption(OptionType.STRING, "group", "Название нужной группы для остановки отслеживания", true)
        ).queue();
    }
}