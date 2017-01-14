package com.power.oj.core.bean;

import com.power.oj.core.OjConfig;
import com.power.oj.problem.ProblemModel;
import com.power.oj.user.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FpsProblem {

    private ProblemModel problemModel;
    private File dataDir;
    private String solution;
    private String solutionLang = "C++";
    private boolean isSpj = false;
    private List<FpsImage> imageList = new ArrayList<FpsImage>();
    private List<String> dataIn = new ArrayList<String>();
    private List<String> dataOut = new ArrayList<String>();

    public FpsProblem() {
        this(0, true);
    }

    public FpsProblem(Integer outputLimit, Boolean status) {
        problemModel = new ProblemModel();
        problemModel.setStatus(status);
        problemModel.setUid(UserService.me().getCurrentUid());
        problemModel.setCtime(OjConfig.timeStamp);
        problemModel.setMtime(OjConfig.timeStamp);
        problemModel.save();
    }

    public FpsProblem(ProblemModel problem) {
        problemModel = problem;
    }

    public FpsProblem(Integer pid) {
        problemModel = ProblemModel.dao.findById(pid);
    }

    public ProblemModel getModel() {
        return problemModel;
    }

    public void setModel(ProblemModel problemModel) {
        this.problemModel = problemModel;
    }

    public boolean isSpj() {
        return isSpj;
    }

    public void setSpj(boolean spj) {
        this.isSpj = spj;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<FpsImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<FpsImage> imageList) {
        this.imageList = imageList;
    }

    public List<String> getDataIn() {
        return dataIn;
    }

    public void setDataIn(List<String> dataIn) {
        this.dataIn = dataIn;
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public String getSolutionLang() {
        return solutionLang;
    }

    public void setSolutionLang(String solutionLang) {
        this.solutionLang = solutionLang;
    }

    public List<String> getDataOut() {
        return dataOut;
    }

    public void setDataOut(List<String> dataOut) {
        this.dataOut = dataOut;
    }

}
