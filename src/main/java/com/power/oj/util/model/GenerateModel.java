package com.power.oj.util.model;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateModel {
    public static void main(String[] args) throws SQLException, IOException, TemplateException {
        Configuration config = new Configuration(new Version(2, 3, 21));
        config.setClassForTemplateLoading(GenerateModel.class, "");
        config.setLocalizedLookup(false);
        config.setDirectoryForTemplateLoading(new File("src/main/java/com/power/oj/util/model"));

        int count = 0;
        Template temp = config.getTemplate("model.ftl");
        Map<String, Table> map = new HashMap<String, Table>();
        List<String> tables = DBConn.getTableNamesByDBName();
        Table myModel = new Table();

        myModel.setPackageName(DBConn.p.getProperty("package"));
        myModel.setPrefix(DBConn.p.getProperty("prefix"));
        myModel.setSuffix(DBConn.p.getProperty("suffix"));

        for (String table : tables) {
            myModel.setTableName(table);
            myModel.setColumns(DBConn.getColumnsInfoByTableName(table));
            map.put("myModel", myModel);

            File createFolder = new File(
                System.getProperty("user.dir") + "/src/main/java/" + DBConn.p.getProperty("package").replace(".", "/"));
            createFolder.mkdirs();
            temp.process(map, new FileWriter(createFolder + "/" + myModel.getModelName() + ".java"));

            count += 1;
        }

        System.out.println("Generated " + count + " tables, package: " + DBConn.p.getProperty("package"));
    }
}
