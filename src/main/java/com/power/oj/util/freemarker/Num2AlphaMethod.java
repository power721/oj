package com.power.oj.util.freemarker;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

public class Num2AlphaMethod implements TemplateMethodModel {

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List args) throws TemplateModelException {
        Integer number = Integer.parseInt(args.get(0).toString());

        if (number >= 0 && number < 26) {
            char c = (char) (number + 'A');
            return c;
        }

        return null;
    }

}
