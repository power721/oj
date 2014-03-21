package com.power.oj.contest;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConstants;

public class AddContestValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    int type = c.getParaToInt("contest.type");
    if (type == ContestModel.TYPE_PASSWORD)
    {
      validateRequired("contest.password", "passMsg", c.getText("contest.add.validate.pass"));
    }
    
    // TODO validate time
  }

  @Override
  protected void handleError(Controller c)
  {
    c.keepModel(ContestModel.class, "contest");
    c.keepPara(OjConstants.PAGE_TITLE);

    c.render("add.html");
  }

}
