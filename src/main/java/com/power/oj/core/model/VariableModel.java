package com.power.oj.core.model;

import com.jfinal.plugin.activerecord.Model;

public class VariableModel extends Model<VariableModel> {
    public static final VariableModel dao = new VariableModel();
    public static final String TABLE_NAME = "variable";
    public static final String ID = "id";
    public static final String CID = "cid";
    public static final String NAME = "name";
    public static final String STRING_VALUE = "stringValue";
    public static final String BOOLEAN_VALUE = "booleanValue";
    public static final String INT_VALUE = "intValue";
    public static final String TEXT_VALUE = "textValue";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    private static final long serialVersionUID = 1L;

    /*
     * auto generated getter and setter
     */
    public Integer getId() {
        return getInt(ID);
    }

    public VariableModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public VariableModel setCid(Integer value) {
        return set(CID, value);
    }

    public String getName() {
        return getStr(NAME);
    }

    public VariableModel setName(String value) {
        return set(NAME, value);
    }

    public String getStringValue() {
        return getStr(STRING_VALUE);
    }

    public VariableModel setStringValue(String value) {
        return set(STRING_VALUE, value);
    }

    public Boolean getBooleanValue() {
        return getBoolean(BOOLEAN_VALUE);
    }

    public VariableModel setBooleanValue(Boolean value) {
        return set(BOOLEAN_VALUE, value);
    }

    public Integer getIntValue() {
        return getInt(INT_VALUE);
    }

    public VariableModel setIntValue(Integer value) {
        return set(INT_VALUE, value);
    }

    public String getTextValue() {
        return getStr(TEXT_VALUE);
    }

    public VariableModel setTextValue(String value) {
        return set(TEXT_VALUE, value);
    }

    public String getType() {
        return getStr(TYPE);
    }

    public VariableModel setType(String value) {
        return set(TYPE, value);
    }

    public String getDescription() {
        return getStr(DESCRIPTION);
    }

    public VariableModel setDescription(String value) {
        return set(DESCRIPTION, value);
    }

}
