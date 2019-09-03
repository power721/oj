package com.power.oj.core;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
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
    public static String astylePath = "/usr/local/bin/astyle";

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

    public static void loadConfig() {
        loadVariable();
        loadLanguage();
        loadLevel();
        OjConfig.judgeVersion = PropKit.get("judge.version", "v1.0");
    }

    public static void loadVariable() {
        variable = new HashMap<>();
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

        astylePath = variable.get("astylePath").getStringValue();

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
        level = new ArrayList<>();
        List<Record> levels = Db.find("SELECT * FROM level ORDER BY level");
        for (Record record : levels) {
            level.add(record.getInt("exp"));
        }
    }

    public static void initJudgeResult() {
        judgeResult = new ArrayList<>();
        judgeResult.add(new ResultType(ResultType.AC, "AC", "Accepted", "通过"));
        judgeResult.add(new ResultType(ResultType.PE, "PE", "Presentation Error", "格式错误"));
        judgeResult.add(new ResultType(ResultType.WA, "WA", "Wrong Answer", "答案错误"));
        judgeResult.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed", "运行超时"));
        judgeResult.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed", "内存超限"));
        judgeResult.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed", "输出超限"));
        judgeResult.add(new ResultType(ResultType.CE, "CE", "Compile Error", "编译错误"));
        judgeResult.add(new ResultType(ResultType.RF, "RF", "Restricted Function", "非法调用"));
        judgeResult.add(new ResultType(ResultType.RE, "RE", "Runtime Error", "运行错误"));
        judgeResult.add(new ResultType(ResultType.SE, "SE", "System Error", "系统错误"));
        judgeResult.add(new ResultType(ResultType.VE, "VE", "Validate Error", "校验错误"));
        judgeResult.add(new ResultType(ResultType.RUN, "RUN", "Running", "正在运行"));
        judgeResult.add(new ResultType(ResultType.WAIT, "WAIT", "Waiting", "正在等待"));
        judgeResult.add(new ResultType(ResultType.REJUDGE, "REJUDGE", "Rejudging", "正在重测"));
        judgeResult.add(new ResultType(ResultType.SIM, "SIM", "Similar", "代码相似"));
        judgeResult.add(new ResultType(ResultType.COM, "COM", "Compiling", "正在编译"));
        judgeResult.add(new ResultType(ResultType.QUE, "QUE", "Queuing", "测评队列中"));
        resultType = new HashMap<>();
        for (ResultType result : judgeResult) {
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
