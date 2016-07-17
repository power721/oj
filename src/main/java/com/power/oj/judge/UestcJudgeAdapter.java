package com.power.oj.judge;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.problem.ProblemModel;
import jodd.format.Printf;
import jodd.io.FileNameUtil;
import jodd.util.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class UestcJudgeAdapter extends JudgeAdapter {

    public UestcJudgeAdapter() {
        super();
    }

    public UestcJudgeAdapter(Solution solution) {
        super(solution);
    }

    @Override
    protected boolean compile() throws IOException {
        return true;
    }

    @Override
    protected boolean runProcess() throws IOException, InterruptedException {
        ProblemModel problemModel;
        if (solution.isContest()) {
            problemModel = problemService.findProblemForContest(solution.getPid());
        } else {
            problemModel = problemService.findProblem(solution.getPid());
        }

        List<String> inFiles = new ArrayList<String>();
        List<String> outFiles = new ArrayList<String>();
        int numOfData = judgeService.getDataFiles(solution.getPid(), inFiles, outFiles);

        int i;
        long timeLimit = problemModel.getTimeLimit();
        long memoryLimit = problemModel.getMemoryLimit();
        //long outputLimit = OjConfig.getInt("outputLimit", 8192);
        boolean isSpj = problemService.checkSpj(solution.getPid());

        if (numOfData < 1) {
            log.warn("No data file for problem " + solution.getPid());
        }
        boolean isAccepted = true;
        setResult(ResultType.RUN, 0, 0);
        for (i = 0; isAccepted && i < numOfData; ++i) {
            String cmd = buildCommand(timeLimit, memoryLimit, isSpj, FileNameUtil.getName(inFiles.get(i)),
                FileNameUtil.getName(outFiles.get(i)), i == 0);
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();

            StringBuilder sb = new StringBuilder();
            InputStream errorStream = process.getErrorStream();
            while (errorStream.available() > 0) {
                Character c = new Character((char) errorStream.read());
                sb.append(c);
            }

            String[] resultStr = stringBuilder.toString().split(" ");
            int exitValue = process.waitFor();
            if (exitValue > 0) {
                updateSystemError(sb.toString());
                return false;
            }
            isAccepted = checkResult(resultStr, sb.toString());
        }

        log.info(Printf.str("%d: Total run time: %d ms", solution.getSid(), solution.getTime()));
        synchronized (JudgeAdapter.class) {
            updateResult(isAccepted, i);
            updateUser();
            if (!updateContest()) {
                updateProblem();
            }
        }
        return true;
    }

    /**
     * Build the pylon core judging command line content.
     *
     * @return command line content
     */
    private String buildCommand(long timeLimit, long memoryLimit, boolean isSpj, String inputFile, String outputFile,
        boolean firstCase) {
        ProgramLanguageModel programLanguage = OjConfig.languageType.get(solution.getLanguage());
        String sourceFileName = OjConstants.SOURCE_FILE_NAME + "" + programLanguage.getExt();
        String workPath = judgeService.getWorkPath(solution);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OjConfig.getString("runShell"));
        stringBuilder.append(" -u ");
        stringBuilder.append(solution.getSid());
        stringBuilder.append(" -s ");
        stringBuilder.append(sourceFileName);
        stringBuilder.append(" -n ");
        stringBuilder.append(solution.getPid());
        stringBuilder.append(" -D ");
        stringBuilder.append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("dataPath")));
        stringBuilder.append(" -d ");
        stringBuilder.append(FileNameUtil.normalizeNoEndSeparator(workPath));
        stringBuilder.append(" -t ");
        stringBuilder.append(timeLimit);
        stringBuilder.append(" -m ");
        stringBuilder.append(memoryLimit);
        //stringBuilder.append(" -o ");
        //stringBuilder.append(outputLimit);
        if (isSpj)
            stringBuilder.append(" -S");
        stringBuilder.append(" -l ");
        stringBuilder.append(solution.getLanguage());
        stringBuilder.append(" -I ");
        stringBuilder.append(inputFile);
        stringBuilder.append(" -O ");
        stringBuilder.append(outputFile);
        if (firstCase)
            stringBuilder.append(" -C");
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private boolean checkResult(String[] resultStr, String errorOut) {
        boolean isAccepted = true;
        if (resultStr != null && resultStr.length >= 3) {
            try {
                int result = convertResult(Integer.parseInt(resultStr[0]));
                if (result == ResultType.AC) {
                    result = ResultType.RUN;
                } else {
                    isAccepted = false;
                }
                int time = Integer.parseInt(resultStr[1]);
                int memory = Integer.parseInt(resultStr[2]);
                setResult(result, time, memory);
            } catch (NumberFormatException e) {
                updateSystemError(e.getLocalizedMessage());

                if (OjConfig.isDevMode())
                    e.printStackTrace();
                log.error(e.getLocalizedMessage());
                isAccepted = false;
            }
        } else {
            updateSystemError(errorOut);
            isAccepted = false;
        }

        if (solution.getResult() == ResultType.CE) {
            updateCompileError(readError("stderr_compiler.txt"));
        } else if (solution.getResult() == ResultType.RE) {
            if (StringUtil.isBlank(errorOut)) {
                errorOut = readError("stderr_executive.txt");
            }
            updateRuntimeError(errorOut);
        }
        return isAccepted;
    }

    private String readError(String fileName) {
        String workPath = judgeService.getWorkPath(solution);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(judgeService.getWorkDirPath(solution) + fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith(workPath)) {
                    line = line.substring(workPath.length());
                }
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            if (OjConfig.isDevMode())
                e.printStackTrace();
            log.error(e.getLocalizedMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    private int convertResult(int result) {
    /*
    const int OJ_WAIT           = 0; //Queue
    const int OJ_AC             = 1; //Accepted
    const int OJ_PE             = 2; //Presentation Error
    const int OJ_TLE            = 3; //Time Limit Exceeded
    const int OJ_MLE            = 4; //Memory Limit Exceeded
    const int OJ_WA             = 5; //Wrong Answer
    const int OJ_OLE            = 6; //Output Limit Exceeded
    const int OJ_CE             = 7; //Compilation Error
    const int OJ_RE_SEGV        = 8; //Segment Violation
    const int OJ_RE_FPE         = 9; //FPU Error
    const int OJ_RE_BUS         = 10;//Bus Error
    const int OJ_RE_ABRT        = 11;//Abort
    const int OJ_RE_UNKNOWN     = 12;//Unknow
    const int OJ_RF             = 13;//Restricted Function
    const int OJ_SE             = 14;//System Error
    const int OJ_RE_JAVA        = 15;//JAVA Run Time Exception
    */
        switch (result) {
            case 0:
                return ResultType.RUN;
            case 1:
                return ResultType.AC;
            case 2:
                return ResultType.PE;
            case 3:
                return ResultType.TLE;
            case 4:
                return ResultType.MLE;
            case 5:
                return ResultType.WA;
            case 6:
                return ResultType.OLE;
            case 7:
                return ResultType.CE;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 15:
                return ResultType.RE;
            case 13:
                return ResultType.RF;
            case 14:
            default:
                return ResultType.SE;
        }
    }

}
