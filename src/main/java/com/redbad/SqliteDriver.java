package com.redbad;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SqliteDriver {
    public static Connection connection;
    public SqliteDriver(String fileName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        createDatabase();
    }

    public void createDatabase() throws SQLException {
        Statement cursor = connection.createStatement();
        cursor.execute("CREATE TABLE IF NOT EXISTS 'groups' ('name' TEXT, 'guild_id' INTEGER, 'channel_id' INTEGER);");
        cursor.execute("CREATE TABLE IF NOT EXISTS 'users' ('id' INTEGER PRIMARY KEY, 'background_link' TEXT);");
        cursor.close();
    }

    public List<Map<String, Object>> sqlSelectData(String fields, String table, String condition) throws SQLException {
        Statement cursor = connection.createStatement();
        ResultSet resultSet = cursor.executeQuery(String.format("SELECT %s FROM '%s' WHERE %s;", fields, table, condition));
        List<Map<String, Object>> resultList = Utils.parseResultSet(resultSet);
        resultSet.close();
        cursor.close();
        return resultList;
    }

    public List<Map<String, Object>> sqlSelectData(String fields, String table) throws SQLException {
        Statement cursor = connection.createStatement();
        ResultSet resultSet = cursor.executeQuery(String.format("SELECT %s FROM '%s';", fields, table));
        List<Map<String, Object>> resultList = Utils.parseResultSet(resultSet);
        resultSet.close();
        cursor.close();
        return resultList;
    }

    public void sqlUpdateData(String table, String settable, String condition) throws SQLException {
        Statement cursor = connection.createStatement();
        cursor.execute(String.format("UPDATE '%s' SET %s WHERE %s;", table, settable, condition));
        cursor.close();
    }

    public void sqlInsertData(String table, Map<String, Object> datas) throws SQLException {
        Statement cursor = connection.createStatement();
        StringBuilder[] buildData = Utils.buildSqliteGroups(datas);
        cursor.execute(String.format("INSERT INTO '%s' %s VALUES %s;", table, buildData[0], buildData[1]));
        cursor.close();
    }

    public void sqlDeleteData(String table, String condition) throws SQLException {
        Statement cursor = connection.createStatement();
        cursor.execute(String.format("DELETE FROM '%s' WHERE %s;", table, condition));
        cursor.close();
    }
}
