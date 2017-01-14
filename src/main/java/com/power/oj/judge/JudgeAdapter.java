package com.power.oj.judge;

import com.google.common.io.Files;
import com.jfinal.log.Logger;
import com.power.oj.contest.ContestService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.core.service.SessionService;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;
import jodd.io.FileUtil;
import jodd.util.StringUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JudgeAdapter implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(JudgeAdapter.class);
    protected static final JudgeService judgeService = JudgeService.me();
    protected static final ContestService contestService = ContestService.me();
    protected static final UserService userService = UserService.me();
    protected static final ProblemService problemService = ProblemService.me();
    private static final Pattern classNamePattern =
        Pattern.compile("\\s*(?:public)?\\s*(?:final)?\\s+class\\s+(\\w+)\\s*");
    private static final int RESERVED_TEMP_DIRS = 25;
    protected final Logger log = Logger.getLogger(getClass());
    protected Solution solution;
    private boolean deleteTempDir = false;

    public JudgeAdapter() {
        super();
    }

    public JudgeAdapter(Solution solution) {
        this();
        this.solution = solution;
    }

    protected abstract boolean compile() throws IOException;

    protected abstract boolean runProcess() throws IOException, InterruptedException;

    @Override
    public void run() {
        synchronized (JudgeAdapter.class) {
            try {
                prepare();
                if (compile()) {
                    runProcess();
                } else {
                    log.warn("Compile failed.");
                }
            } catch (IOException e) {
                updateSystemError(e.getLocalizedMessage());
                // backupTempDir4SE();

                log.error("IOException in judge.", e);
            } catch (Exception e) {
                updateSystemError(e.getLocalizedMessage());
                // backupTempDir4SE();

                log.error("Exception in judge.", e);
            }
        }
    }

    protected void prepare() throws IOException, URISyntaxException {
        ProgramLanguageModel programLanguage = OjConfig.languageType.get(solution.getLanguage());

        String workPath = judgeService.getWorkPath(solution);
        if (solution instanceof SolutionModel && !needDeleteTempDir() && OjConfig.getBoolean("deleteTmpFile", false)) {
            File prevWorkDir = new File(workPath + (solution.getSid() - RESERVED_TEMP_DIRS));
            if (prevWorkDir.isDirectory()) {
                try {
                    FileUtil.deleteDir(prevWorkDir);
                    log.debug("Delete previous work directory " + prevWorkDir.getAbsolutePath());
                } catch (IOException e) {
                    log.warn("Delete previous work directory failed!", e);
                }
            }
        }

        File workDir = new File(workPath + solution.getSid());
        if (workDir.isDirectory()) {
            try {
                FileUtil.cleanDir(workDir);
                log.debug("Clean directory: " + workDir);
            } catch (IOException e) {
                log.warn("Clean directory failed!", e);
            }
        } else {
            FileUtil.mkdirs(workDir);
            log.debug("Make directory: " + workDir);
        }
        java.nio.file.Files.setPosixFilePermissions(workDir.toPath(), JudgeService.FILE_PERMISSIONS);

        String workDirPath = workDir.getAbsolutePath();

        if (solution.getLanguage().equals(OjConfig.languageID.get("Java"))) {
            String className = getJavaPublicClass(solution.getSource());
            log.debug("className = " + className);
            if (className != null && !className.equals(OjConstants.SOURCE_FILE_NAME)) {
                generateClassFiles(programLanguage, workDirPath, className);
                return;
            }
        }

        File sourceFile = new File(getFilePath(programLanguage, workDirPath, OjConstants.SOURCE_FILE_NAME));
        FileUtil.touch(sourceFile);
        String content =
            solution.getSource().replaceAll("#\\s*include\\s*\"\\.*/.*\".*", "#error \"Your action is logged!\"")
                .replaceAll("#\\s*include\\s*<\\.*/.*>.*", "#error \"Your action is logged!\"");
        if (content.length() != solution.getSource().length()) {
            LOGGER.warn(
                "User id " + solution.getUid() + " from " + SessionService.me().getHost() + " try to hack system!");
        }
        FileUtil.writeString(sourceFile, content);

        log.debug("Create source file: " + sourceFile);
    }

    private String getFilePath(ProgramLanguageModel programLanguage, String workDirPath, String name) {
        return workDirPath + File.separator + name + '.' + programLanguage.getExt();
    }

    private String getJavaPublicClass(String content) {
        String className = null;
        Matcher matcher = classNamePattern.matcher(content);
        if (matcher.find()) {
            className = matcher.group(1);
        }
        return className;
    }

    private void generateClassFiles(ProgramLanguageModel programLanguage, String workDirPath, String className)
        throws IOException, URISyntaxException {

        generateUserClass(programLanguage, workDirPath, className);

        generateMainClass(programLanguage, workDirPath, className);
    }

    private void generateUserClass(ProgramLanguageModel programLanguage, String workDirPath, String className)
        throws IOException {
        File sourceFile = new File(getFilePath(programLanguage, workDirPath, className));
        FileUtil.touch(sourceFile);
        String content = solution.getSource().replaceAll("\\s*package\\s+.*;", "");
        FileUtil.writeString(sourceFile, content);
        log.debug("Create source file: " + sourceFile);
    }

    private void generateMainClass(ProgramLanguageModel programLanguage, String workDirPath, String className)
        throws IOException, URISyntaxException {
        File sourceFile = new File(getFilePath(programLanguage, workDirPath, OjConstants.SOURCE_FILE_NAME));
        FileUtil.touch(sourceFile);
        File file = new File(getClass().getResource("/Main.ftl").toURI());
        String content = FileUtils.readFileToString(file).replace("${_CLASS_NAME_}", className);
        FileUtil.writeString(sourceFile, content);
        log.debug("Create source file: " + sourceFile);
    }

    protected boolean updateCompileError(String error) {
        solution.setResult(ResultType.CE);
        if (StringUtil.isNotBlank(error)) {
            SolutionService.checkCompileError(solution, error);
        }

        return solution.update();
    }

    protected boolean updateRuntimeError(String error) {
        solution.setResult(ResultType.RE);
        if (StringUtil.isNotBlank(error)) {
            solution.setError(error);
        }

        return solution.update();
    }

    protected boolean updateSystemError(String error) {
        solution.setResult(ResultType.SE);
        if (StringUtil.isNotBlank(error)) {
            solution.setSystemError(error);
        }

        judgeService.cleanRejudge(solution);
        return solution.update();
    }

    protected boolean updateResult(int result, int time, int memory) {
        solution.setResult(result).setTime(time).setMemory(memory);

        return solution.update();
    }

    protected boolean updateResult(boolean ac, Integer test) {
        if (ac) {
            solution.setResult(ResultType.AC);
        } else if (solution.getResult() != ResultType.CE && solution.getResult() != ResultType.RF) {
            solution.setTest(test);
        }

        return solution.update();
    }

    protected boolean setResult(int result, int time, int memory) {
        boolean needUpdate = result != solution.getResult();
        solution.setResult(result);
        if (solution.getTime() == null) {
            solution.setTime(time);
        } else {
            solution.setTime(time + solution.getTime());
        }
        if (solution.getMemory() == null) {
            solution.setMemory(memory);
        } else {
            solution.setMemory(Math.max(memory, solution.getMemory()));
        }

        if (!needUpdate) {
            return true;
        }

        return solution.update();
    }

    protected boolean setResult(int result, int time, int memory, int test) {
        solution.setResult(result);
        solution.setTime(time);
        solution.setMemory(memory);
        solution.setTest(test);

        return solution.update();
    }

    protected boolean updateUser() {
        if (solution.getResult() != ResultType.AC) {
            return false;
        }

        Integer cid = solution.getCid();
        if (cid != null && cid > 0) {
            return false;
        }
        return userService.incAccepted(solution);
    }

    protected boolean updateProblem() {
        if (solution.getResult() != ResultType.AC) {
            return false;
        }

        return problemService.incAccepted(solution);
    }

    protected boolean updateContest() {
        if (solution.isContest()) {
            if (solution.get("originalResult") != null) {
                contestService.updateBoard4Rejudge(solution, solution.getInt("originalResult"));
            } else {
                contestService.updateBoard(solution);
            }
            return true;
        }
        return false;
    }

    public boolean needDeleteTempDir() {
        return deleteTempDir;
    }

    public void setDeleteTempDir(boolean deleteTempDir) {
        this.deleteTempDir = deleteTempDir;
    }

}
