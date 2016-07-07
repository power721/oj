package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ResultType;
import com.power.oj.solution.SolutionService;

import java.io.File;

public class JudgeApiController extends OjController {

    private static final Logger LOGGER = Logger.getLogger(JudgeApiController.class);

    @Before(POST.class)
    public void updateResult() {
        UploadFile uploadFile = getFile("error", "", 10 * 1024, "UTF-8");
        File file = null;
        if (uploadFile != null) {
            file = uploadFile.getFile();
        }
        int result = getParaToInt("result");
        int sid = getParaToInt("sid");
        int cid = getParaToInt("cid");
        int time = 0;
        int memory = 0;
        int test = 0;

        if (result != ResultType.CE && result != ResultType.SE) {
            time = getParaToInt("time");
            memory = getParaToInt("memory");
            test = getParaToInt("test");
        }

        LOGGER.info("sid=" + sid + " cid=" + cid + " result=" + result + " time=" + time+ " memory=" + memory + " test=" + test);
        if(SolutionService.me().setResult(sid, cid, result, time, memory, test, file)) {
            LOGGER.debug("set result successfully.");
        } else {
            LOGGER.error("set result failed!");
        }
        renderNull();
    }

}
