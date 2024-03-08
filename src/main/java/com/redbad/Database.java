package com.redbad;
import com.redbad.objects.Group;
import com.redbad.utils.SqliteDriver;
import org.redbad.DescWeek;
import org.redbad.Parser;

import java.sql.*;
import java.util.*;

public class Database {
    public Map<String, Group> groups_list = new HashMap<>();
    public SqliteDriver driver;
    public boolean preloaded;
    public Database(String fileName) throws SQLException, ClassNotFoundException {
        driver = new SqliteDriver(fileName);
        preloaded = preloadFromDB();
    }

    public boolean preloadFromDB() {
        try {
            for (Map<String, Object> group : driver.sqlSelectData("*", "groups")) {
                Object name = group.get("name");
                Parser parser = new Parser();
                parser.getHTMLModel("");
                if (!parser.requestBody.isDone) {
                    return false;
                }
                DescWeek obj = parser.get_desc_by_group(parser.get_groups().get(name.toString())).get_week();
                addGroup((String) name, obj, parser.get_desc_by_group(obj.nextWeekHREF).get_week(), (Long) group.get("guild_id"), (Long) group.get("channel_id"));
            }
            return true;
        } catch (Exception err) {
            System.out.println(err.fillInStackTrace().toString());
        }
        return false;
    }

    public void addGroup(String name, DescWeek total_week, DescWeek next_week, long guild_id, long channel_id) {
        if (!groups_list.containsKey(name)) {
            Group group = new Group(name, total_week, next_week);
            group.addChat(guild_id, channel_id);
            groups_list.put(name, group);
        } else {
            groups_list.get(name).addChat(guild_id, channel_id);
        }
    }

    public void addSqlGroup(String name, DescWeek total_week, DescWeek next_week, long guild_id, long channel_id) {
        addGroup(name, total_week, next_week, guild_id, channel_id);
        Map<String, Object> datas = new HashMap<>();
        datas.put("name", name);
        datas.put("guild_id", guild_id);
        datas.put("channel_id", channel_id);
        try {
            driver.sqlInsertData("groups", datas);
        } catch (SQLException err) {
            System.out.println(err.fillInStackTrace().toString());
        }
    }

    public void delGroup(String name, long guild_id, long channel_id) {
        if (groups_list.get(name).chats.size() > 1) {
            groups_list.get(name).delChat(guild_id, channel_id);
        } else {
            groups_list.remove(name);
        }

        try {
            driver.sqlDeleteData("groups", String.format("name='%s' AND guild_id=%s AND channel_id=%s", name, guild_id, channel_id));
        } catch (SQLException err) {
            System.out.println(err.fillInStackTrace().toString());
        }
    }

    public boolean chatAndGroupExists(String name, long guild_id, long channel_id) {
        if (groups_list.containsKey(name)) {
            return groups_list.get(name).chatExists(guild_id, channel_id); }
        else
            return false;
    }
}
