package com.power.oj.util.model;

public class Column {
    private String name;
    private String field;
    private String type;

    public Column(String name, String type) {
        this.name = name;
        this.field = convertFieldName(name);
        this.type = converType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String convertFieldName(String name) {
        boolean flag = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            String tmp = String.valueOf(c);
            if (Character.isLowerCase(c)) {
                flag = true;
            } else {
                if (flag && Character.isUpperCase(c)) {
                    sb.append("_");
                }
                flag = false;
            }

            sb.append(tmp);
        }
        return sb.toString();
    }

    private String converType(String type) {
        if ("tinyint(1)".equals(type))
            return "Boolean";
        int pos = type.indexOf('(');
        if (pos > 0) {
            type = type.substring(0, pos);
        }

        switch (type) {
            case "varchar":
            case "char":
            case "enum":
            case "set":
            case "text":
            case "tinytext":
            case "mediumtext":
            case "longtext":
                return "String";
            case "bit":
                return "Boolean";
            case "int":
            case "integer":
            case "tinyint":
            case "smallint":
            case "mediumint":
                return "Integer";
            case "date":
            case "year":
                return "java.sql.Date";
            case "time":
                return "java.sql.Time";
            case "timestamp":
            case "datetime":
                return "java.sql.Timestamp";
            case "unsigned bigint":
                return "java.math.BigInteger";
            case "decimal":
            case "numeric":
                return "java.math.BigDecimal";
            case "bigint":
                return "Long";
            case "float":
                return "Float";
            case "real":
            case "double":
                return "Double";
            case "binary":
            case "varbinary":
            case "tinyblob":
            case "blob":
            case "mediumblob":
            case "longblob":
                return "byte[]";
            default:
                return null;
        }
    }

    public String getMethod() {
        switch (type) {
            case "String":
                return "getStr";
            case "Integer":
                return "getInt";
            case "java.sql.Date":
                return "getDate";
            case "java.sql.Time":
                return "getTime";
            case "java.sql.Timestamp":
                return "getTimestamp";
            case "java.math.BigInteger":
                return "getBigInteger";
            case "java.math.BigDecimal":
                return "getBigDecimal";
            case "byte[]":
                return "getBytes";
            default:
                return "get" + type;
        }
    }
}
