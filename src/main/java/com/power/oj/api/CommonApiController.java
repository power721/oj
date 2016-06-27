package com.power.oj.api;

import com.jfinal.aop.Clear;
import com.power.oj.core.OjController;

public class CommonApiController extends OjController {

    @Clear
    public void empty() {
        renderNull();
    }

}
