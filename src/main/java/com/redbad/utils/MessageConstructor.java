package com.redbad.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.redbad.DescDay;
import org.redbad.DescLesson;

import java.util.List;
import java.util.Map;


public class MessageConstructor {
    public MessageCreateBuilder builder;

    public MessageConstructor() {
        this.builder = new MessageCreateBuilder();
    }

    public void buildStringSelect(String selectId, Map<String, String> options, String name, boolean keyUpper) {
        StringSelectMenu.Builder constructor = StringSelectMenu.create(selectId);
        constructor.setPlaceholder(name);
        for (Map.Entry<String, String> map_element : options.entrySet()) {
            constructor.addOption(keyUpper ? map_element.getKey().toUpperCase() : map_element.getKey(), map_element.getValue());
        }
        builder.addActionRow(constructor.build());
    }

    public void buildStringSelect(String selectId, List<String> options, String name, boolean keyUpper) {
        StringSelectMenu.Builder constructor = StringSelectMenu.create(selectId);
        constructor.setPlaceholder(name);
        for (String element : options) {
            constructor.addOption(keyUpper ? element.toUpperCase() : element, Integer.toString(options.indexOf(element)));
        }
        builder.addActionRow(constructor.build());
    }

    public void buildEmbedByLesson(DescDay day, DescLesson lesson, String college_url, String group_url) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(Utils.firstUpperCase(Utils.weekdayPattern.format(day.date)) + " " + Utils.datePattern.format(day.date));
        embed.setColor(lesson.bgcolor);
        embed.setAuthor("Расписание занятий | " + day.groupName, college_url + group_url);
        embed.setFooter("Источник: " + college_url);
        embed.addField("№ пары", lesson.number, false);
        embed.addField("Время занятий", lesson.timeline, false);
        embed.addField("Способ", lesson.processType, false);
        embed.addField("Дисциплина, преподаватель", lesson.discipline, false);
        if (!lesson.theme.isEmpty())
            embed.addField("Тема занятия", lesson.theme, false);
        if (!lesson.resources.isEmpty())
            embed.addField("Ресурс", lesson.resources, false);
        if (!lesson.tasks.isEmpty())
            embed.addField("Задание для выполнения", lesson.tasks, false);
        builder.addEmbeds(embed.build());
    }

    public void addButton() {
        builder.addActionRow(
                Button.primary("test1", "ПРЕД. НЕДЕЛЯ"),
                Button.primary("test2", "НАЗАД"),
                Button.primary("test3", "СЛЕД. НЕДЕЛЯ")
        );
    }

    public void addContent(String content) {
        builder.addContent(content);
    }

    public MessageCreateData build() {
        return builder.build();
    }

    public MessageEditData buildAsEdit() {
        return MessageEditData.fromCreateData(build());
    }
}
