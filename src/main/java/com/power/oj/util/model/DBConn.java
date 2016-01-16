package com.power.oj.util.model;

import com.mysql.jdbc.Statement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConn {
    public static final Properties p = new Properties();

    static {
        try {
            p.load(DBConn.class.getResourceAsStream("/createEntity.properties"));
            Class.forName(p.getProperty("className"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws IOException, SQLException {
        return DriverManager.getConnection(p.getProperty("jdbcUrl"), p.getProperty("user"), p.getProperty("password"));
    }

    public static List<String> getTableNamesByDBName() throws SQLException, IOException {
        Statement stame = (Statement) DBConn.getConnection().createStatement();
        ResultSet rs = stame.executeQuery("SHOW TABLES;");
        List<String> list = new ArrayList<String>();
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        return list;
    }

    public static List<String> getColumnsInfoByTableName(String tName) throws SQLException, IOException {
        List<String> list = new ArrayList<String>();
        Statement stame = (Statement) DBConn.getConnection().createStatement();
        ResultSet rs = stame.executeQuery("DESC " + tName + ";");
        while (rs.next()) {
            list.add(rs.getString(1) + ";" + rs.getString(2));
        }
        return list;
    }
}
