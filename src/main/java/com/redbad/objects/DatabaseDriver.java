package com.redbad.objects;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface DatabaseDriver {
    void createDatabase() throws SQLException;
    List<Map<String, Object>> sqlSelectData(String fields, String table, String condition) throws SQLException;
    void sqlUpdateData(String table, String settable, String condition) throws SQLException;
    void sqlInsertData(String table, Map<String, Object> datas) throws SQLException;
    void sqlDeleteData(String table, String condition) throws SQLException;
}
