package com.power.oj.util.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String packageName;
    private String tableName;
    private String prefix;
    private String modelName;
    private String suffix;
    private List<Column> columns;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        setModelName(tableName);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String tableName) {
        modelName = new StringBuilder().append(prefix).append(toUpperCamelCase(tableName)).append(suffix).toString();
        modelName = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
        System.out.println("Table Name: " + tableName + "  -->  Model Name: " + modelName);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix.trim();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix.trim();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columnsNames) {
        columns = new ArrayList<Column>();
        for (String name : columnsNames) {
            String fields[] = name.split(";");
            columns.add(new Column(fields[0], fields[1]));
        }
    }

    private String toUpperCamelCase(String name) {
        boolean flag = true;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            String tmp = String.valueOf(c);
            if (flag) {
                tmp = tmp.toUpperCase();
                flag = false;
            }

            if (c == '_') {
                flag = true;
            } else {
                sb.append(tmp);
            }
        }
        return sb.toString();
    }
}
