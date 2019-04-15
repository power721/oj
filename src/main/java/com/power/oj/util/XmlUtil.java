package com.power.oj.util;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import jodd.util.SystemUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public abstract class XmlUtil {

    private static final Logger log = Logger.getLogger(XmlUtil.class);
    private static final String DTD_FILE = "http://git.oschina.net/power/oj/raw/master/fps.dtd";

    public static Document readDocument(File file) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        return saxReader.read(file);
    }

    public static Element createXmlRootElement(Document document) {
        //document.addDocType("fps", "-//FreeProblemSet//EN", DTD_FILE); ///出现验证错误，暂时注释
        Element rootElement = document.addElement("fps");
        rootElement.addAttribute("version", "1.1");
        rootElement.addAttribute("url", "http://code.google.com/p/freeproblemset/");

        Element generator = rootElement.addElement("generator");
        generator.addAttribute("name", OjConfig.getString("siteTitle", "PowerOJ"));
        generator.addAttribute("url", OjConfig.getString("domaiNname", "http://git.oschina.net/power/oj"));

        return rootElement;
    }

    public static File exportXmlFile(Document document, final String fileName) {
        File file = new File(SystemUtil.getTempDir(), fileName);
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            OutputStream out = new FileOutputStream(file);
            Writer fileWriter = new OutputStreamWriter(out, "UTF-8");
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);

            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("create XML file failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }
        return file;
    }
}
