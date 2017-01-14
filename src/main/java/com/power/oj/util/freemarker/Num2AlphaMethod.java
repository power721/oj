package com.power.oj.util.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

public class Num2AlphaMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(List args) throws TemplateModelException {
        Integer number = Integer.parseInt(args.get(0).toString());

        if (number >= 0 && number < 26) {
            return (char) (number + 'A');
        }

        return null;
    }

}
