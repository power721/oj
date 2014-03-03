package com.power.oj.judge;

import java.io.IOException;

import com.power.oj.solution.SolutionModel;

public interface JudgeAdapter
{
  String DATA_EXT_IN = ".in";
  String DATA_EXT_OUT = ".out";
  
  boolean Compile(SolutionModel solutionModel) throws IOException;
  
  boolean RunProcess(SolutionModel solutionModel) throws IOException, InterruptedException;
  
}
