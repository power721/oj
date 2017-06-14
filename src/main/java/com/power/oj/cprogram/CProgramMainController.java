package com.power.oj.cprogram;

import com.jfinal.aop.Before;
import com.power.oj.core.OjController;

/**
 * Created by w703710691d on 2017/6/14.
 */
@Before(CProgramInterceptor.class)
public class CProgramMainController extends OjController {
    public void index() {

    }
}
