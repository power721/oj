package com.power.oj.discussion;

import com.power.oj.core.OjController;

public class DiscussionController extends OjController
{

  public void index()
  {
    setTitle(getText("discuss.index.title"));
  }
}
