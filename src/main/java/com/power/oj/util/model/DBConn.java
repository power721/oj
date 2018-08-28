package com.power.oj.util.model;


import java.io.IOException;
import java.sql.*;
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
        Statement statement = null;
        Connection connection = null;
        try {
            connection = DBConn.getConnection();
            statement = (Statement) connection.createStatement();
            ResultSet rs = statement.executeQuery("SHOW TABLES;");
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {

                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    public static List<String> getColumnsInfoByTableName(String tName) throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        Statement statement = null;
        Connection connection = null;
        try {
            connection = DBConn.getConnection();
            statement = (Statement) connection.createStatement();
            ResultSet rs = statement.executeQuery("DESC " + tName + ";");
            while (rs.next()) {
                list.add(rs.getString(1) + ";" + rs.getString(2));
            }
            return list;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {

                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}
