package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jodd.io.FileUtil;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.problem.ProblemModel;
import com.power.oj.user.UserService;

public class FpsProblem
{
  private final Logger log = Logger.getLogger(FpsProblem.class);
  private ProblemModel problemModel;
  private File dataDir;
  private long outputLimit = 8192 * 1024;
  private String solution;
  private String solutionLang;
  private boolean spj = false;
  private List<FpsImage> imageList;
  private List<String> dataIn;
  private List<String> dataOut;

  public FpsProblem()
  {
    problemModel = new ProblemModel();
    problemModel.setUid(UserService.me().getCurrentUid());
    problemModel.setCtime(OjConfig.timeStamp);
    problemModel.setMtime(OjConfig.timeStamp).save();
    
    imageList = new ArrayList<FpsImage>();
    dataIn = new ArrayList<String>();
    dataOut = new ArrayList<String>();
  }
  
  public FpsProblem(Integer outputLimit, Boolean status)
  {
    problemModel = new ProblemModel();
    problemModel.setStatus(status);
    problemModel.setUid(UserService.me().getCurrentUid());
    problemModel.setCtime(OjConfig.timeStamp);
    problemModel.setMtime(OjConfig.timeStamp).save();
    
    imageList = new ArrayList<FpsImage>();
    dataIn = new ArrayList<String>();
    dataOut = new ArrayList<String>();
    this.outputLimit = outputLimit;
  }
  
  public FpsProblem itemToProblem(Node item)
  {
    problemModel = ProblemModel.dao.findById(problemModel.getPid());
    NodeList ch = item.getChildNodes();
    int num = 0;
    int timeLimit;
    int memoryLimit;
    String unit;

    StringBuilder sb = new StringBuilder(7).append(OjConfig.get("dataPath")).append(File.separator).append(problemModel.getPid());
    dataDir = new File(sb.toString());
    try
    {
      FileUtil.mkdirs(dataDir);
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    
    for (int i = 0; i < ch.getLength(); i++)
    {
      Node e = ch.item(i);
      String name = e.getNodeName();
      String value = e.getTextContent();
      if (name.equalsIgnoreCase("title"))
      {
        problemModel.setTitle(value);
      }
      if (name.equalsIgnoreCase("time_limit"))
      {
        timeLimit = Integer.parseInt(value);
        if (e.getAttributes() != null)
        {
          unit = e.getAttributes().getNamedItem("unit").getNodeValue();
          if ("s".equalsIgnoreCase(unit))
          {
            timeLimit *= 1000;
          }
        }
        problemModel.setTimeLimit(timeLimit);
      }
      if (name.equalsIgnoreCase("memory_limit"))
      {
        memoryLimit = Integer.parseInt(value);
        if (e.getAttributes() != null)
        {
          unit = e.getAttributes().getNamedItem("unit").getNodeValue();
          if ("mb".equalsIgnoreCase(unit))
          {
            memoryLimit *= 1024;
          }
        }
        problemModel.setMemoryLimit(memoryLimit);
      }
      if (name.equalsIgnoreCase("description"))
      {
        problemModel.setDescription(value);
      }
      if (name.equalsIgnoreCase("input"))
      {
        problemModel.setInput(value);
      }
      if (name.equalsIgnoreCase("output"))
      {
        problemModel.setOutput(value);
      }
      if (name.equalsIgnoreCase("sample_input"))
      {
        problemModel.setSampleInput(value);
      }
      if (name.equalsIgnoreCase("sample_output"))
      {
        problemModel.setSampleOutput(value);
      }
      if (name.equalsIgnoreCase("test_input"))
      {
        dataIn.add(value);
      }
      if (name.equalsIgnoreCase("test_output"))
      {
        dataOut.add(value);
      }
      if (name.equalsIgnoreCase("hint"))
      {
        problemModel.setHint(value);
      }
      if (name.equalsIgnoreCase("source"))
      {
        problemModel.setSource(value);
      }
      if (name.equalsIgnoreCase("solution"))
      {
        solution = value;
        if (e.getAttributes() != null)
        {
          solutionLang = e.getAttributes().getNamedItem("language").getNodeValue();
        }
        saveSourceFile(String.valueOf(problemModel.getPid()), solutionLang, solution);
      }
      if (name.equalsIgnoreCase("spj"))
      {
        String lang = "c++";
        if (e.getAttributes() != null)
        {
          lang = e.getAttributes().getNamedItem("language").getNodeValue();
          spj = true;
        }
        saveSourceFile("spj", lang, value);
      }
      if (name.equalsIgnoreCase("img"))
      {
        imageList.add(new FpsImage(e, this, num++));
      }
    }
    problemModel.setDescription(setImages(problemModel.getDescription()));
    problemModel.setInput(setImages(problemModel.getInput()));
    problemModel.setOutput(setImages(problemModel.getOutput()));
    problemModel.setHint(setImages(problemModel.getHint()));
    problemModel.update();
    
    svaeData();
    
    return this;
  }
  
  public boolean testSource()
  {
    // TODO
    return true;
  }

  private String setImages(String html)
  {
    for (Iterator<FpsImage> i = imageList.iterator(); i.hasNext();)
    {
      FpsImage img = (FpsImage) i.next();
      html = html.replaceAll(img.getOldURL(), img.getURL());
    }

    return html;
  }

  private void saveSourceFile(String name, String lang, String source)
  {
    String ext = "cc";
    if ("c".equalsIgnoreCase(lang))
    {
      ext = "c";
    }
    else if ("pascal".equalsIgnoreCase(lang))
    {
      ext = "pas";
    }
    else if ("java".equalsIgnoreCase(lang))
    {
      ext = "java";
    }
    else if ("python".equalsIgnoreCase(lang))
    {
      ext = "py";
    }
    
    StringBuilder sb = new StringBuilder(7).append(OjConfig.get("dataPath")).append(File.separator);
    sb.append(problemModel.getPid()).append(File.separator).append(name).append(".").append(ext).toString();
    File sourceFile = new File(sb.toString());
    
    try
    {
      FileUtil.touch(sourceFile);
      FileUtil.writeString(sourceFile, source);
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
  }

  private void svaeData()
  {
    int n = 0;
    Iterator<String> in = dataIn.iterator();
    Iterator<String> it = dataOut.iterator();
    for (; in.hasNext() && it.hasNext(); n++)
    {
      StringBuilder sb = new StringBuilder(6).append(dataDir.getAbsolutePath()).append(File.separator);
      sb.append(problemModel.getPid()).append("_").append(n);
      File dataInFile = new File(sb.toString() + ".in");
      File dataOutFile = new File(sb.toString() + ".out");
      
        String inData = (String) in.next();
        String outData = (String) it.next();
        
        try
        {
            FileUtil.touch(dataOutFile);
            FileUtil.writeString(dataOutFile, outData);
            
            FileUtil.touch(dataInFile);
            FileUtil.writeString(dataInFile, inData);
            log.info(dataInFile.getAbsolutePath());
            log.info(dataOutFile.getAbsolutePath());
            if (dataOutFile.length() > outputLimit)
            {
              log.warn("data output file is too large: " + dataOutFile.length());
              FileUtil.delete(dataInFile);
              FileUtil.delete(dataOutFile);
            }
        } catch (IOException e)
        {
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.error(e.getLocalizedMessage());
        }
    }
  }
  
  public ProblemModel getProblemModel()
  {
    return problemModel;
  }

  public void setProblemModel(ProblemModel problemModel)
  {
    this.problemModel = problemModel;
  }

  public boolean isSpj()
  {
    return spj;
  }

  public void setSpj(boolean spj)
  {
    this.spj = spj;
  }

  public String getSolution()
  {
    return solution;
  }

  public void setSolution(String solution)
  {
    this.solution = solution;
  }

  public List<FpsImage> getImageList()
  {
    return imageList;
  }

  public void setImageList(List<FpsImage> imageList)
  {
    this.imageList = imageList;
  }

  public List<String> getDataIn()
  {
    return dataIn;
  }

  public void setDataIn(List<String> dataIn)
  {
    this.dataIn = dataIn;
  }

}
