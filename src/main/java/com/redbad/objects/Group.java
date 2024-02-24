package com.redbad.objects;

import org.redbad.DescObject;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public String group_name;
    public List<String> chats;
    public DescWeek total_week;
    public DescWeek next_week;

    public Group(String group_name, DescWeek total_week, DescWeek next_week) {
        this.group_name = group_name;
        this.total_week = total_week;
        this.next_week = next_week;
        this.chats = new ArrayList<>();
    }

    public void addChat(int chatId) {
        chats.add(String.valueOf(chatId));
    }

    public void delChat(int chatId) {
        chats.remove(String.valueOf(chatId));
    }

    public boolean chatExists(int chatId) {
        return chats.contains(String.valueOf(chatId));
    }
}
