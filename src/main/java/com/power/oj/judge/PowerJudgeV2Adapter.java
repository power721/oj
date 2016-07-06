package com.power.oj.judge;

import com.jfinal.log.Logger;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.problem.ProblemModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class PowerJudgeV2Adapter extends PowerJudgeAdapter {

    private static final Logger LOGGER = Logger.getLogger(PowerJudgeV2Adapter.class);

    public PowerJudgeV2Adapter(Solution solution) {
        super(solution);
    }

    @Override
    protected boolean compile() throws IOException {
        return true;
    }

    @Override
    protected boolean runProcess() throws IOException, InterruptedException {
        ProblemModel problemModel;
        int cid = 0;

        if (solution instanceof ContestSolutionModel) {
            problemModel = problemService.findProblemForContest(solution.getPid());
            cid = solution.getCid();
        } else {
            problemModel = problemService.findProblem(solution.getPid());
        }

        if (problemModel == null) {
            setResult(ResultType.SE, 0, 0);
            updateSystemError("Cannot find problem " + solution.getPid());
            return false;
        }

        int timeLimit = problemModel.getTimeLimit();
        int memoryLimit = problemModel.getMemoryLimit();

        String hostName = OjConfig.getString("judgeHost", "127.0.0.1");
        int portNumber = OjConfig.getInt("judgePort", 12345);

        Socket socket = null;
        try {
            socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.write(OjConfig.getString("judgeSecurity", ""));
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            char[] buff = new char[256];
            LOGGER.info("waiting message!");
            int num = in.read(buff);
            if (num == -1) {
                LOGGER.error("read socket error");
                throw new SocketException("read socket error");
            } else {
                String reply = new String(buff, 0, num);
                LOGGER.info(reply);
                if (!"Authentication Ok.".equals(reply)) {
                    throw new JudgeConfigurationException(reply);
                }
            }

            out.write(
                solution.getSid() + " " + cid + " " + solution.getPid() + " " + solution.getLanguage() + " " + timeLimit
                    + " " + memoryLimit);
            out.flush();

            LOGGER.info("waiting reply!");
            num = in.read(buff);
            if (num == -1) {
                LOGGER.error("read socket error");
                throw new SocketException("read socket error");
            } else {
                String reply = new String(buff, 0, num);
                LOGGER.info(reply);
                if (!"I got your request.".equals(reply)) {
                    throw new JudgeConfigurationException(reply);
                }
            }
            setResult(ResultType.RUN, 0, 0);
            return true;
        } catch (JudgeConfigurationException e) {
            LOGGER.error("judge configuration error", e);
            updateSystemError(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("judge error, try to use PowerJudgeAdapter", e);
            super.runProcess();
        } finally {
            closeSocket(socket);
        }
        return false;
    }

    private void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {

            }
        }
    }

}
