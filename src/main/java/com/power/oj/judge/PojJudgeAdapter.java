package com.power.oj.judge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.problem.ProblemModel;

public class PojJudgeAdapter extends JudgeAdapter {

	public PojJudgeAdapter() {
		super();
	}

	public PojJudgeAdapter(Solution solution) {
		super(solution);
	}

	public boolean compile() throws IOException {
		log.info(solution.getSid() + " Start compiling...");

		ProgramLanguageModel programLanguage = OjConfig.languageType.get(solution.getLanguage());
		String workDirPath = judgeService.getWorkDirPath(solution);
		String comShellName = OjConfig.getString("compileShell");
		String compileCmdName = getCompileCmd(programLanguage.getCompileCmd(), workDirPath, OjConstants.SOURCE_FILE_NAME,
				programLanguage.getExt());
		log.info("compileCmd: " + compileCmdName);

		/*
		 * execute compiler command
		 */
		Process compileProcess = Runtime.getRuntime().exec(comShellName);
		OutputStream comShellOutputStream = compileProcess.getOutputStream();
		comShellOutputStream.write(compileCmdName.getBytes());
		comShellOutputStream.flush();

		BufferedReader compileErrorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
		long startTime = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		String errorOutput = "";
		while ((startTime + 10000L > System.currentTimeMillis())
				&& ((errorOutput = compileErrorBufferedReader.readLine()) != null)) {
			sb.append(errorOutput).append("\n");
		}
		compileErrorBufferedReader.close();

		int ret = 0;
		try {
			ret = compileProcess.waitFor();
		} catch (InterruptedException e) {
			if (OjConfig.isDevMode())
				e.printStackTrace();
			log.warn("Compile Process is interrupted: " + e.getLocalizedMessage());
		}

		File mainProgram = new File(new StringBuilder(4).append(workDirPath).append(File.separator)
				.append(OjConstants.SOURCE_FILE_NAME).append(".").append(programLanguage.getExe()).toString());
		log.info(mainProgram.getAbsolutePath());
		boolean success = mainProgram.isFile();

		if (!success) {
			updateCompileError(sb.toString());
			log.warn("Compile failed, return value: " + ret);
		}

		return success;
	}

	public boolean runProcess() throws IOException, InterruptedException {
		log.info(solution.getSid() + " RunProcess...");
		ProgramLanguageModel programLanguage = OjConfig.languageType.get(solution.getLanguage());
		String workDirPath = judgeService.getWorkDirPath(solution);

		/*
		 * execute run command
		 */
		Process runProcess = Runtime.getRuntime().exec(OjConfig.getString("runShell"));
		OutputStream runProcessOutputStream = runProcess.getOutputStream();
		log.info("runProcess: " + OjConfig.getString("runShell"));

		ProblemModel problemModel;
		if (solution instanceof ContestSolutionModel) {
			problemModel = problemService.findProblemForContest(solution.getPid());
		} else {
			problemModel = problemService.findProblem(solution.getPid());
		}

		List<String> inFiles = new ArrayList<String>();
		List<String> outFiles = new ArrayList<String>();
		int numOfData = judgeService.getDataFiles(solution.getPid(), inFiles, outFiles);

		long timeLimit = problemModel.getTimeLimit() * programLanguage.getTimeFactor() + numOfData
				* programLanguage.getExtTime();
		long caseTimeLimit = problemModel.getTimeLimit() * programLanguage.getTimeFactor() + programLanguage.getExtTime();
		runProcessOutputStream.write((timeLimit + "\n").getBytes());
		runProcessOutputStream.write((caseTimeLimit + "\n").getBytes());

		long memoryLimit = (problemModel.getMemoryLimit() + programLanguage.getExtMemory()) * 1024L;
		runProcessOutputStream.write((memoryLimit + "\n").getBytes());
		log.info("timeLimit: " + timeLimit);
		log.info("caseTimeLimit: " + caseTimeLimit);
		log.info("memoryLimit: " + memoryLimit);

		String mainProgram = new StringBuilder(6).append(workDirPath).append(File.separator)
				.append(OjConstants.SOURCE_FILE_NAME).append(".").append(programLanguage.getExe()).append("\n").toString();
		runProcessOutputStream.write(mainProgram.getBytes());
		runProcessOutputStream.write((workDirPath + "\n").getBytes());
		log.info("mainProgram: " + mainProgram);

		runProcessOutputStream.write((numOfData + "\n").getBytes());
		log.info("data files: " + numOfData);
		if (numOfData < 1) {
			log.warn("No data file for problem " + solution.getPid());
		}
		for (int i = 0; i < numOfData; ++i) {
			runProcessOutputStream.write((inFiles.get(i) + "\n").getBytes());
			log.info(inFiles.get(i));
			String userOutFile = new StringBuilder(4).append(workDirPath).append(File.separator)
					.append(new File(outFiles.get(i)).getName()).append("\n").toString();
			runProcessOutputStream.write(userOutFile.getBytes());
			log.info(userOutFile);
			runProcessOutputStream.write((outFiles.get(i) + "\n").getBytes());
			log.info(outFiles.get(i));
		}
		runProcessOutputStream.flush();

		BufferedReader inputStreamBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));

		String buff = inputStreamBufferedReader.readLine();
		log.info("original time: " + buff);
		int time = Integer.parseInt(buff);
		buff = inputStreamBufferedReader.readLine();
		log.info("original memory: " + buff);
		int memory = Integer.parseInt(buff);
		if (memory > 0)
			memory -= programLanguage.getExtMemory();
		StringBuilder sb = new StringBuilder();

		InputStream errorStream = runProcess.getErrorStream();
		while (errorStream.available() > 0) {
			Character c = new Character((char) errorStream.read());
			sb.append(c);
		}
		String errorOut = sb.toString();
		log.info("errorOut: " + errorOut);

		runProcessOutputStream.close();
		inputStreamBufferedReader.close();
		errorStream.close();

		int result = runProcess.waitFor();
		result = runProcess.exitValue();
		log.info("original result: " + result);
		if (result != ResultType.AC && (errorOut.indexOf("Exception") != -1 || errorOut.indexOf("Traceback") != -1)
				|| errorOut.indexOf("Runtime error") != -1) {
			result = ResultType.RE;
		}
		if (result < 0) {
			result = ResultType.SE;
		}
		log.info("result: " + result + "  time: " + time + "  memory: " + memory);

		synchronized (JudgeAdapter.class) {
			boolean ret = updateResult(result, time, memory);
			updateUser();
			if (!updateContest()) {
				updateProblem();
			}

			return ret;
		}
	}

	private String getCompileCmd(String compileCmd, String path, String name, String ext) {
		path = new StringBuilder(2).append(path).append(File.separator).toString();
		compileCmd = StringUtil.replace(compileCmd, "%PATH%", path);
		compileCmd = StringUtil.replace(compileCmd, "%NAME%", name);
		compileCmd = StringUtil.replace(compileCmd, "%EXT%", ext);
		compileCmd = new StringBuilder(2).append(compileCmd).append("\n").toString();

		return compileCmd;
	}

	public static void main(String[] args) {

	}

}
