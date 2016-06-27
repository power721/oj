package com.power.oj.api;

import com.power.oj.core.OjController;

public class CommonApiController extends OjController {

    public void empty() {
        renderNull();
    }

    public void time() {
        renderText(String.valueOf(System.currentTimeMillis()));
    }

}
