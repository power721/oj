package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import jodd.util.StringUtil;

import org.dom4j.Element;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public class FpsProblem
{
  private final Logger log = Logger.getLogger(FpsProblem.class);
  private ProblemModel problemModel;
  private File dataDir;
  private long outputLimit = 8192 * 1024;
  private String solution;
  private String solutionLang = "C++";
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

  public FpsProblem(ProblemModel problem)
  {
    problemModel = problem;
    imageList = new ArrayList<FpsImage>();
    dataIn = new ArrayList<String>();
    dataOut = new ArrayList<String>();
  }
  
  public FpsProblem(Integer pid)
  {
    problemModel = ProblemModel.dao.findById(pid);
    imageList = new ArrayList<FpsImage>();
    dataIn = new ArrayList<String>();
    dataOut = new ArrayList<String>();
  }
  
  public FpsProblem itemToProblem(Element item)
  {
    problemModel = ProblemModel.dao.findById(problemModel.getPid());
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
    
    for (Iterator<?> i = item.elementIterator(); i.hasNext();)
    {
      Element e = (Element) i.next();
      String name = e.getName();
      String value = e.getText();
      if (name.equalsIgnoreCase("title"))
      {
        problemModel.setTitle(value);
      }
      if (name.equalsIgnoreCase("time_limit"))
      {
        timeLimit = Integer.parseInt(value);
        unit = e.attributeValue("unit");
        if ("s".equalsIgnoreCase(unit))
        {
          timeLimit *= 1000;
        }
        problemModel.setTimeLimit(timeLimit);
      }
      if (name.equalsIgnoreCase("memory_limit"))
      {
        memoryLimit = Integer.parseInt(value);
        unit = e.attributeValue("unit");
        if ("mb".equalsIgnoreCase(unit))
        {
          memoryLimit *= 1024;
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
        solutionLang = e.attributeValue("language");
        saveSourceFile(String.valueOf(problemModel.getPid()), solutionLang, solution);
      }
      if (name.equalsIgnoreCase("spj"))
      {
        String lang = e.attributeValue("language");
        spj = true;
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
  
  public Element problemToItem(Element item)
  {
    Element title = item.addElement("title");
    title.addCDATA(problemModel.getTitle());
    
    Element timeLimit = item.addElement("time_limit");
    timeLimit.addAttribute("unit", "ms");
    timeLimit.addCDATA(String.valueOf(problemModel.getTimeLimit()));

    Element memoryLimit = item.addElement("memory_limit");
    memoryLimit.addAttribute("unit", "kb");
    memoryLimit.addCDATA(String.valueOf(problemModel.getMemoryLimit()));
    
    Element description = item.addElement("description");
    description.addCDATA(problemModel.getDescription().replaceAll("\n", "<br>\n"));
    
    String inputValue = problemModel.getInput();
    if (inputValue != null && inputValue.length() > 0)
    {
      Element input = item.addElement("input");
      input.addCDATA(inputValue.replaceAll("\n", "<br>\n"));
    }

    String outputValue = problemModel.getOutput();
    if (outputValue != null && outputValue.length() > 0)
    {
      Element output = item.addElement("output");
      output.addCDATA(outputValue.replaceAll("\n", "<br>\n"));
    }

    Element sample_input = item.addElement("sample_input");
    sample_input.addCDATA(problemModel.getSampleInput());

    Element sample_output = item.addElement("sample_output");
    sample_output.addCDATA(problemModel.getSampleOutput());
    
    {
      try
      {
        addTestData(item);
        addSolution(item);
        addSpj(item);
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      addImages(item);
    }

    String hintValue = problemModel.getHint();
    if (hintValue != null && hintValue.length() > 0)
    {
      Element hint = item.addElement("hint");
      hint.addCDATA(hintValue.replaceAll("\n", "<br>\n"));
    }

    String sourceValue = problemModel.getSource();
    if (sourceValue != null && sourceValue.length() > 0)
    {
      Element source = item.addElement("source");
      source.addCDATA(sourceValue);
    }

    return item;
  }
  
  public boolean testSource()
  {
    if (solution == null)
    {
      return false;
    }
    
    return true;
  }
  
  private Element addTestData(Element item) throws IOException
  {
    StringBuilder sb = new StringBuilder(7).append(OjConfig.get("dataPath")).append(File.separator).append(problemModel.getPid());
    dataDir = new File(sb.toString());
    if (!dataDir.isDirectory())
    {
      throw new IOException("Data files does not exist.");
    }
    
    File[] arrayOfFile = dataDir.listFiles();
    for (int i = 0; i < arrayOfFile.length; i++)
    {
      File inFile = arrayOfFile[i];
      if (!inFile.getName().toLowerCase().endsWith(OjConstants.DATA_EXT_IN))
        continue;
      
      File outFile = new File(new StringBuilder().append(dataDir.getAbsolutePath()).append(File.separator)
          .append(inFile.getName().substring(0, inFile.getName().length() - OjConstants.DATA_EXT_IN.length())).append(OjConstants.DATA_EXT_OUT).toString());
      if (!outFile.isFile())
      {
        log.warn("Output file for input file does not exist: " + inFile.getAbsolutePath());
        continue;
      }
      Element testInput = item.addElement("test_input");
      testInput.addCDATA(FileUtil.readString(inFile));
      
      Element testOutput = item.addElement("test_output");
      testOutput.addCDATA(FileUtil.readString(outFile));
    }
    return item;
  }

  private Element addSolution(Element item) throws IOException
  {
    File[] arrayOfFile = dataDir.listFiles();
    String[] exts = {"c", "cc", "pas", "java", "py"};
    for (int i = 0; i < arrayOfFile.length; i++)
    {
      String ext = FileNameUtil.getExtension(arrayOfFile[i].getName());
      if (StringUtil.equalsOne(ext, exts) != -1)
      {
        Element solution = item.addElement("solution");
        solution.addCDATA(FileUtil.readString(arrayOfFile[i]));
        solution.addAttribute("language", ext2lang(ext));
        return item;
      }
    }
    
    SolutionModel solutionModel = SolutionModel.dao.findFirst("SELECT * FROM solution s WHERE pid=? AND result=? ORDER BY time,memory LIMIT 1",
        problemModel.getPid(), ResultType.AC);
    if (solutionModel != null)
    {
      Element solution = item.addElement("solution");
      solution.addCDATA(solutionModel.getSource());
      solution.addAttribute("language", ext2lang(OjConfig.language_type.get(solutionModel.getLanguage()).getExt()));
    }
    
    return item;
  }

  private String ext2lang(String ext)
  {
    if (ext.equalsIgnoreCase("c"))
    {
      return "C";
    }
    else if (ext.equalsIgnoreCase("cc") || ext.equalsIgnoreCase("cpp"))
    {
      return "C++";
    }
    else if (ext.equalsIgnoreCase("pas"))
    {
      return "Pascal";
    }
    else if (ext.equalsIgnoreCase("java"))
    {
      return "Java";
    }
    else if (ext.equalsIgnoreCase("py"))
    {
      return "Python";
    }
    return "";
  }
  
  private Element addSpj(Element item) throws IOException
  {
    File spjFile = new File(dataDir.getAbsolutePath() + File.separator + "spj");
    if (!spjFile.isFile())
    {
      spjFile = new File(dataDir.getAbsolutePath() + File.separator + "Validate.exe");
    }
    
    if (spjFile.isFile())
    {
      File[] arrayOfFile = dataDir.listFiles();
      String[] files = {"spj.c", "spj.cc", "spj.java"/*, "spj.pas", "spj.py"*/};
      for (int i = 0; i < arrayOfFile.length; i++)
      {
        String name = arrayOfFile[i].getName();
        String ext = FileNameUtil.getExtension(name);
        if (StringUtil.equalsOne(name, files) != -1)
        {
          Element solution = item.addElement("solution");
          solution.addCDATA(FileUtil.readString(arrayOfFile[i]));
          solution.addAttribute("language", ext2lang(ext));
          return item;
        }
      }
    }
    return item;
  }

  private Element addImages(Element item)
  {
    // TODO
    return item;
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
    if (dataIn.size() < 1)
    {
      saveSampleData();
      return;
    }
    
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
  
  private void saveSampleData()
  {
    StringBuilder sb = new StringBuilder(3).append(dataDir.getAbsolutePath()).append(File.separator).append("sample");
    File dataInFile = new File(sb.toString() + ".in");
    File dataOutFile = new File(sb.toString() + ".out");
    
    try
    {
      FileUtil.touch(dataOutFile);
      FileUtil.writeString(dataOutFile, problemModel.getSampleOutput());
      
      FileUtil.touch(dataInFile);
      FileUtil.writeString(dataInFile, problemModel.getSampleInput());
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
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
