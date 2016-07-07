package com.power.oj.core;

import com.jfinal.core.JFinal;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.util.FileKit;
import jodd.util.SystemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Configure the system.
 *
 * @author power
 */
public class OjConfig {
    public static String siteTitle = null;
    public static String userAvatarPath = null;
    public static String problemImagePath = null;
    public static String uploadPath = null;
    public static String downloadPath = null;
    public static String webRootPath = null;
    public static String judgeVersion = "v1.0";

    public static int contestPageSize = 20;
    public static int contestRankPageSize = 50;
    public static int problemPageSize = 50;
    public static int userPageSize = 20;
    public static int friendPageSize = 10;
    public static int statusPageSize = 20;
    public static int mailGroupPageSize = 10;
    public static int mailPageSize = 20;
    public static int noticePageSize = 20;
    public static int newsPageSize = 4;
    public static int timeStamp;
    public static long startInterceptorTime;
    public static long startGlobalHandlerTime;
    public static boolean variableChanged = false;
    public static List<ProgramLanguageModel> programLanguages;
    public static Map<Integer, ProgramLanguageModel> languageType;
    public static Map<Integer, String> languageName;
    public static Map<String, Integer> languageID;
    public static Map<Integer, ResultType> resultType;
    public static List<ResultType> judgeResult;
    public static List<Integer> level;
    private static String baseURL = null;
    private static Map<String, VariableModel> variable;
    private static boolean bIsLinux;
    private static AppConfig appConfig;

    private static final Logger LOGGER = Logger.getLogger(OjConfig.class);

    static {
        bIsLinux = SystemUtil.getOsName().contains("Linux");
    }
    // TODO use enChahe

    public static boolean isDevMode() {
        return JFinal.me().getConstants().getDevMode();
    }

    public static boolean isLinux() {
        return bIsLinux;
    }

    public static void setAppConfig(AppConfig appConfig) {
        OjConfig.appConfig = appConfig;
    }

    public static void loadConfig() {
        loadVariable();
        loadLanguage();
        loadLevel();
        OjConfig.judgeVersion = appConfig.getProperty("judge.version", "v1.0");
    }

    public static void loadVariable() {
        variable = new HashMap<String, VariableModel>();
        for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable")) {
            variable.put(variableModel.getName(), variableModel);
        }

        siteTitle = getString("siteTitle", "Power OJ");

        webRootPath = FileKit.parsePath(getString("webRootPath", "/var/www/"), true);
        uploadPath = FileKit.parsePath(webRootPath + "/upload/", true);
        LOGGER.info("uploadPath: " + uploadPath);
        downloadPath = FileKit.parsePath(webRootPath + "/download/", true);
        LOGGER.info("downloadPath: " + downloadPath);
        userAvatarPath = FileKit.parsePath(webRootPath + "/upload/image/user/", true);
        LOGGER.info("userAvatarPath: " + userAvatarPath);
        problemImagePath = FileKit.parsePath(webRootPath + "/upload/image/problem/", true);
        LOGGER.info("problemImagePath: " + problemImagePath);

        contestPageSize = getInt("contestPageSize", 20);
        contestRankPageSize = getInt("contestRankPageSize", 50);
        problemPageSize = getInt("problemPageSize", 50);
        userPageSize = getInt("userPageSize", 20);
        statusPageSize = getInt("statusPageSize", 20);
        variableChanged = false;
    }

    public static void loadLanguage() {
        languageType = new HashMap<>();
        languageName = new HashMap<>();
        languageID = new HashMap<>();
        programLanguages = ProgramLanguageModel.dao.find("SELECT * FROM program_language WHERE status=1");
        for (ProgramLanguageModel language : programLanguages) {
            languageType.put(language.getId(), language);
            languageID.put(language.getName(), language.getId());
            languageName.put(language.getId(), language.getName());
        }
    }

    public static void loadLevel() {
        level = new ArrayList<Integer>();
        List<Record> levels = Db.find("SELECT * FROM level ORDER BY level");
        for (Record record : levels) {
            level.add(record.getInt("exp"));
        }
    }

    public static void initJudgeResult() {
        judgeResult = new ArrayList<ResultType>();
        judgeResult.add(new ResultType(ResultType.AC, "AC", "Accepted"));
        judgeResult.add(new ResultType(ResultType.PE, "PE", "Presentation Error"));
        judgeResult.add(new ResultType(ResultType.WA, "WA", "Wrong Answer"));
        judgeResult.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed"));
        judgeResult.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed"));
        judgeResult.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed"));
        judgeResult.add(new ResultType(ResultType.CE, "CE", "Compile Error"));
        judgeResult.add(new ResultType(ResultType.RF, "RF", "Restricted Function"));
        judgeResult.add(new ResultType(ResultType.RE, "RE", "Runtime Error"));
        judgeResult.add(new ResultType(ResultType.SE, "SE", "System Error"));
        judgeResult.add(new ResultType(ResultType.VE, "VE", "Validate Error"));
        judgeResult.add(new ResultType(ResultType.RUN, "RUN", "Running"));
        judgeResult.add(new ResultType(ResultType.WAIT, "WAIT", "Waiting"));

        resultType = new HashMap<Integer, ResultType>();
        for (Iterator<ResultType> it = judgeResult.iterator(); it.hasNext(); ) {
            ResultType result = it.next();
            resultType.put(result.getId(), result);
        }
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static void setBaseURL(String baseUrl) {
        OjConfig.baseURL = baseUrl;
    }

    /*
     * get OJ variable from DB cache
     */
    public static VariableModel get(String name) {
        return variable.get(name);
    }

    public static String getString(String name) {
        return getString(name, null);
    }

    public static String getString(String name, String defaultValue) {
        VariableModel model = variable.get(name);
        if (model != null) {
            return model.getStringValue();
        }

        return defaultValue;
    }

    public static Integer getInt(String name) {
        return getInt(name, null);
    }

    public static Integer getInt(String name, Integer defaultValue) {
        VariableModel model = variable.get(name);
        if (model != null) {
            return model.getIntValue();
        }

        return defaultValue;
    }

    public static Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }

    public static Boolean getBoolean(String name, Boolean defaultValue) {
        VariableModel model = variable.get(name);
        if (model != null) {
            return model.getBooleanValue();
        }

        return defaultValue;
    }

    public static String getText(String name) {
        return variable.get(name).getTextValue();
    }

    public static String getType(String name) {
        return variable.get(name).getType();
    }

}
