package com.redbad;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDABuilder builder = JDABuilder.createDefault("MTA4MDgwMDkyNzc1MTA4MjAwNA.GE2NJS.wxHlNU2A5lLJljhTchAvmk9iJJjr2egpfDuJzQ");
        Database database = new Database("database.sqlite");

        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(new AddGroupListener(), new DescListener(), new DelGroupListener());
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