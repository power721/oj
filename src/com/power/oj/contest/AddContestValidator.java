package com.power.oj.contest;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;

public class AddContestValidator extends Validator
{

  @Override
  protected void handleError(Controller c)
  {
    c.keepModel(ContestModel.class, "contest");
    c.keepPara(OjConstants.PAGE_TITLE);

    c.render("add.html");
  }

  @Override
  protected void validate(Controller c)
  {
    int type = c.getParaToInt("contest.type");
    if (type == 3)
    {
      validateRequired("contest.pass", "passMsg", "You must set a password!");
    }
  }

}
