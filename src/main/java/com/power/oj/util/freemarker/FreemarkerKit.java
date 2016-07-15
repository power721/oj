package com.power.oj.util.freemarker;

import com.jfinal.kit.PathKit;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.RenderException;
import com.power.oj.core.AppConfig;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-11-11 12:46 AM
 * @since JDK 1.5
 */
public class FreemarkerKit {

    // 配置
    private static Configuration appConfig = null;

    static {
        getAppConfiguration();
    }

    /**
     * appConfig配置所有参数 重写freemarker中的 reader方法，读取该配置文件
     *
     * @return config
     */
    private static Configuration getAppConfiguration() {
        if (appConfig == null) {
            // 从freemarker 视图中获取所有配置
            appConfig = (Configuration) FreeMarkerRender.getConfiguration().clone();
            try {
                // 设置模板路径
                appConfig
                    .setDirectoryForTemplateLoading(new File(PathKit.getWebRootPath() + AppConfig.getBaseViewPath()));
                appConfig.setObjectWrapper(new DefaultObjectWrapperBuilder(new Version(2, 3, 21)).build());
            } catch (IOException e) {
                // TODO log
            }
        }
        return appConfig;
    }

    /**
     * 渲染模版为字符串，并制定参数
     *
     * @param tpl   模版
     * @param model 参数信息
     * @return 渲染后的字符串
     */
    public static String processString(String tpl, Map<String, Object> model) {
        if (appConfig == null) {
            getAppConfiguration();
        }
        StringWriter result = new StringWriter();
        try {
            Template template = appConfig.getTemplate(tpl);
            template.process(model, result);
        } catch (IOException e) {
            throw new RenderException(e);
        } catch (TemplateException e) {
            throw new RenderException(e);
        }
        return result.toString();
    }
}
