package com.power.oj.util.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class Num2SizeMethod implements TemplateMethodModel
{

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List args) throws TemplateModelException
  {
    Long size = Long.parseLong(args.get(0).toString());

    String str = String.format("%d", size) + " B";
    if (size >= 1048576)
      str = String.format("%.2f", size / 1048576.) + " MB";
    else if (size >= 1024)
      str = String.format("%.2f", size / 1024.) + " KB";
    
    return str;
  }

}
