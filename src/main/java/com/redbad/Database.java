package com.redbad;
import com.redbad.objects.Group;

import java.sql.*;
import java.util.*;

public class Database {
    public Map<Integer, Group> groups_list;
    public SqliteDriver driver;
    public Database(String fileName) throws SQLException, ClassNotFoundException {
        driver = new SqliteDriver(fileName);
    }
}
