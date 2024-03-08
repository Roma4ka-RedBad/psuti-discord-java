package com.redbad.objects;

import com.redbad.utils.Utils;
import org.redbad.DescWeek;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public String group_name;
    public List<Long[]> chats;
    public DescWeek total_week;
    public DescWeek next_week;

    public Group(String group_name, DescWeek total_week, DescWeek next_week) {
        this.group_name = group_name;
        this.total_week = total_week;
        this.next_week = next_week;
        this.chats = new ArrayList<>();
    }

    public void addChat(long guild_id, long channel_id) {
        chats.add(new Long[]{guild_id, channel_id});
    }

    public void delChat(long guild_id, long channel_id) {
        chats.remove(new Long[]{guild_id, channel_id});
    }

    public boolean chatExists(long guild_id, long channel_id) {
        return Utils.containsArray(chats, new Long[]{guild_id, channel_id});
    }
}
