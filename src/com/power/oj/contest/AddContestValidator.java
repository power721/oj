package com.power.oj.contest;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class AddContestValidator extends Validator
{

	@Override
	protected void handleError(Controller c)
	{
		// TODO Auto-generated method stub
		c.keepModel(ContestModel.class, "contest");
		c.keepPara("pageTitle");
		
		c.render("add.html");
	}

	@Override
	protected void validate(Controller c)
	{
		// TODO Auto-generated method stub
		int type = c.getParaToInt("contest.type");
		if(type == 3)
		{
			validateRequired("contest.pass", "passMsg", "You must set a password!");
		}
	}

}
