package com.power.oj.util.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class AsciiMethod implements TemplateMethodModel
{

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List args) throws TemplateModelException
  {
    Integer number = Integer.parseInt(args.get(0).toString());
    
    if (number >= 0 && number < 26)
    {
      char c = (char) (number + 'A');
      return c;
    }
    else if (number >= 'A' && number <= 'Z')
    {
      char c = (char) (number + 'A' - 'A');
      return c;
    }
    return null;
  }

}
