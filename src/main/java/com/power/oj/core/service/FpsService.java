package com.power.oj.core.service;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.FpsImage;
import com.power.oj.core.bean.FpsProblem;
import com.power.oj.core.bean.ResultType;
import com.power.oj.judge.JudgeService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionModel;
import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import jodd.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FpsService {

    private static final Logger log = Logger.getLogger(FpsService.class);
    private FpsProblem problem;

    public void setFpsProblem(FpsProblem problem) {
        this.problem = problem;
    }

    public FpsProblem itemToProblem(Element item) {
        problem.setModel(ProblemModel.dao.findById(problem.getModel().getPid()));
        int num = 0;
        int timeLimit;
        int memoryLimit;
        String unit;

        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + problem.getModel().getPid());
        problem.setDataDir(dataDir);
        try {
            FileUtil.mkdirs(dataDir);
            Files.setPosixFilePermissions(dataDir.toPath(), JudgeService.FILE_PERMISSIONS);
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("mkdir: " + dataDir + " failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }

        for (Iterator<?> i = item.elementIterator(); i.hasNext(); ) {
            Element e = (Element) i.next();
            String name = e.getName();
            String value = e.getText();
            if (name.equalsIgnoreCase("title")) {
                problem.getModel().setTitle(value);
            }
            if (name.equalsIgnoreCase("time_limit")) {
                timeLimit = Integer.parseInt(value);
                unit = e.attributeValue("unit");
                if ("s".equalsIgnoreCase(unit)) {
                    timeLimit *= 1000;
                }
                problem.getModel().setTimeLimit(timeLimit);
            }
            if (name.equalsIgnoreCase("memory_limit")) {
                memoryLimit = Integer.parseInt(value);
                unit = e.attributeValue("unit");
                if ("mb".equalsIgnoreCase(unit)) {
                    memoryLimit *= 1024;
                }
                problem.getModel().setMemoryLimit(memoryLimit);
            }
            if (name.equalsIgnoreCase("description")) {
                problem.getModel().setDescription(value);
            }
            if (name.equalsIgnoreCase("input")) {
                problem.getModel().setInput(value);
            }
            if (name.equalsIgnoreCase("output")) {
                problem.getModel().setOutput(value);
            }
            if (name.equalsIgnoreCase("sample_input")) {
                problem.getModel().setSampleInput(value);
            }
            if (name.equalsIgnoreCase("sample_output")) {
                problem.getModel().setSampleOutput(value);
            }
            if (name.equalsIgnoreCase("test_input")) {
                problem.getDataIn().add(value);
            }
            if (name.equalsIgnoreCase("test_output")) {
                problem.getDataOut().add(value);
            }
            if (name.equalsIgnoreCase("hint")) {
                problem.getModel().setHint(value);
            }
            if (name.equalsIgnoreCase("source")) {
                problem.getModel().setSource(value);
            }
            if (name.equalsIgnoreCase("solution")) {
                problem.setSolution(value);
                problem.setSolutionLang(e.attributeValue("language"));
                saveSourceCodeToFile(String.valueOf(problem.getModel().getPid()), problem.getSolutionLang(),
                    problem.getSolution());
            }
            if (name.equalsIgnoreCase("spj")) {
                String lang = e.attributeValue("language");
                problem.setSpj(true);
                saveSourceCodeToFile("spj", lang, value);
            }
            if (name.equalsIgnoreCase("img")) {
                problem.getImageList().add(saveImage(e, num++));
            }
        }
        problem.getModel().setDescription(setImages(problem.getModel().getDescription()));
        problem.getModel().setInput(setImages(problem.getModel().getInput()));
        problem.getModel().setOutput(setImages(problem.getModel().getOutput()));
        problem.getModel().setHint(setImages(problem.getModel().getHint()));
        problem.getModel().update();

        saveData();

        return problem;
    }

    public Element problemToItem(Element item, Boolean needReplace) {
        Element title = item.addElement("title");
        title.addCDATA(problem.getModel().getTitle());

        Element timeLimit = item.addElement("time_limit");
        timeLimit.addAttribute("unit", "ms");
        timeLimit.addCDATA(String.valueOf(problem.getModel().getTimeLimit()));

        Element memoryLimit = item.addElement("memory_limit");
        memoryLimit.addAttribute("unit", "kb");
        memoryLimit.addCDATA(String.valueOf(problem.getModel().getMemoryLimit()));

        Element description = item.addElement("description");
        String desc = problem.getModel().getDescription();
        if (needReplace && desc != null) {
            desc = desc.replace("\n", "<br>");
        }
        description.addCDATA(desc);

        String inputValue = problem.getModel().getInput();
        if (inputValue != null && inputValue.length() > 0) {
            Element input = item.addElement("input");
            if (needReplace) {
                inputValue = inputValue.replace("\n", "<br>");
            }
            input.addCDATA(inputValue);
        }

        String outputValue = problem.getModel().getOutput();
        if (outputValue != null && outputValue.length() > 0) {
            Element output = item.addElement("output");
            if (needReplace) {
                outputValue = outputValue.replace("\n", "<br>");
            }
            output.addCDATA(outputValue);
        }

        Element sampleInput = item.addElement("sample_input");
        sampleInput.addCDATA(problem.getModel().getSampleInput());

        Element sampleOutput = item.addElement("sample_output");
        sampleOutput.addCDATA(problem.getModel().getSampleOutput());

        try {
            addTestData(item);
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("read test data failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }

        String hintValue = problem.getModel().getHint();
        if (hintValue != null && hintValue.length() > 0) {
            Element hint = item.addElement("hint");
            if (needReplace) {
                hintValue = hintValue.replace("\n", "<br>");
            }
            hint.addCDATA(hintValue);
        }

        String sourceValue = problem.getModel().getSource();
        if (sourceValue != null && sourceValue.length() > 0) {
            Element source = item.addElement("source");
            source.addCDATA(sourceValue);
        }

        try {
            addSpj(item);
            addSolution(item);
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("add solution or spj code failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }

        addImages(item);

        return item;
    }

    private Element addTestData(Element item) throws IOException {
        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + problem.getModel().getPid());
        problem.setDataDir(dataDir);

        if (!dataDir.isDirectory()) {
            log.error("Data files does not exist!");
            Element testInput = item.addElement("test_input");
            testInput.addCDATA(problem.getModel().getSampleInput());

            Element testOutput = item.addElement("test_output");
            testOutput.addCDATA(problem.getModel().getSampleOutput());
            return item;
        }

        File[] arrayOfFile = dataDir.listFiles();
        if (arrayOfFile == null) {
            return item;
        }

        for (File inFile : arrayOfFile) {
            if (!isInDataFile(inFile)) {
                continue;
            }

            File outFile = new File(dataDir.getAbsolutePath() + File.separator + getOutDataFileName(inFile));
            if (!outFile.isFile()) {
                log.warn("Output file for input file does not exist: " + inFile.getAbsolutePath());
                continue;
            }
            Element testInput = item.addElement("test_input");
            testInput.addCDATA(FileUtils.readFileToString(inFile));

            Element testOutput = item.addElement("test_output");
            testOutput.addCDATA(FileUtils.readFileToString(outFile));
        }
        return item;
    }

    private Element addSolution(Element item) throws IOException {
        File[] arrayOfFile = problem.getDataDir().listFiles();
        if (arrayOfFile == null) {
            return item;
        }

        String[] exts = {"c", "cc", "pas", "java", "py"};
        for (File file : arrayOfFile) {
            if (!file.getName().startsWith("spj.")) {
                String ext = FileNameUtil.getExtension(file.getName()).toLowerCase();
                if (StringUtil.equalsOne(ext, exts) != -1) {
                    Element solution = item.addElement("solution");
                    solution.addCDATA(FileUtils.readFileToString(file));
                    solution.addAttribute("language", ext2lang(ext));
                    return item;
                }
            }
        }

        SolutionModel solutionModel = SolutionModel.dao
            .findFirst("SELECT * FROM solution s WHERE pid=? AND result=? AND s.status=1 ORDER BY time,memory LIMIT 1",
                problem.getModel().getPid(), ResultType.AC);
        if (solutionModel != null) {
            Element solution = item.addElement("solution");
            solution.addCDATA(solutionModel.getSource());
            solution
                .addAttribute("language", ext2lang(OjConfig.languageType.get(solutionModel.getLanguage()).getExt()));
        }

        return item;
    }

    private Element addSpj(Element item) throws IOException {
        File spjFile = new File(problem.getDataDir().getAbsolutePath() + File.separator + "spj");
        if (!spjFile.isFile()) {
            spjFile = new File(problem.getDataDir().getAbsolutePath() + File.separator + "Validate.exe");
        }

        if (spjFile.isFile()) {
            File[] arrayOfFile = problem.getDataDir().listFiles();
            if (arrayOfFile == null) {
                return item;
            }

            String[] spjFileNames = {"spj.c", "spj.cc", "spj.java"};
            for (File file : arrayOfFile) {
                String name = file.getName();
                String ext = FileNameUtil.getExtension(name).toLowerCase();
                if (StringUtil.equalsOne(name, spjFileNames) != -1) {
                    Element spj = item.addElement("spj");
                    spj.addCDATA(FileUtils.readFileToString(file));
                    spj.addAttribute("language", ext2lang(ext));
                    break;
                }
            }
        }
        return item;
    }

    private Element addImages(Element item) {
        addImage(item, problem.getModel().getDescription());
        addImage(item, problem.getModel().getInput());
        addImage(item, problem.getModel().getOutput());
        addImage(item, problem.getModel().getHint());

        return item;
    }

    private Element addImage(Element item, String text) {
        if (text == null || text.length() < 10) {
            return item;
        }
        String src;
        String base64;
        String rootPath = OjConfig.webRootPath;
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]?([^'\"]+)['\"]?[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);

        while (m.find()) {
            src = m.group(1);
            if (src.startsWith("http")) {
                URL url;
                try {
                    url = new URL(src);
                } catch (MalformedURLException e) {
                    if (OjConfig.isDevMode()) {
                        log.warn("read image src failed!", e);
                    }
                    log.error(e.getLocalizedMessage());
                    continue;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    BufferedImage image = ImageIO.read(url);
                    ImageIO.write(image, "jpg", baos);
                    baos.flush();
                    base64 = Base64.encodeBase64String(baos.toByteArray());
                } catch (IOException e) {
                    if (OjConfig.isDevMode()) {
                        log.warn("write image file failed!", e);
                    }
                    log.error(e.getLocalizedMessage());
                    continue;
                }
            } else {
                try {
                    String path = rootPath;

                    if(path.endsWith("/")) path = path.substring(0, path.length() - 1);
                    if(!src.startsWith("/")) path = path + "/" + src;
                    else path = path + src;

                    base64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(path)));
                } catch (IOException e) {
                    if (OjConfig.isDevMode()) {
                        log.warn("read image file failed!", e);
                    }
                    log.error(e.getLocalizedMessage());
                    continue;
                }
            }

            Element img = item.addElement("img");
            Element srcImg = img.addElement("src");
            srcImg.addCDATA(src);
            Element base64Img = img.addElement("base64");
            base64Img.addCDATA(base64);
        }

        return item;
    }

    private String setImages(String html) {
        if (html == null) {
            return null;
        }
        for (FpsImage img : problem.getImageList()) {
            html = html.replace(img.getOriginalSrc(), img.getSrc());
        }

        return html;
    }

    private FpsImage saveImage(Element e, int num) {
        FpsImage fpsImage = new FpsImage();
        Integer pid = problem.getModel().getPid();

        String originalSrc = e.elementText("src");
        String base64 = e.elementText("base64");
        String fileName = OjConfig.problemImagePath + File.separator + pid + "_" + num + ".png";
        File imageFile = new File(fileName);
        String src = imageFile.getAbsolutePath().replace(OjConfig.webRootPath, "").substring(1);
        fpsImage.setOriginalSrc(originalSrc);
        fpsImage.setSrc(src);

        try {
            byte[] decodeBuffer = Base64.decodeBase64(base64);
            FileUtil.touch(imageFile);
            FileUtil.writeBytes(imageFile, decodeBuffer);
        } catch (IOException e1) {
            if (OjConfig.isDevMode())
                e1.printStackTrace();
            log.error(e1.getLocalizedMessage());
        }

        return fpsImage;
    }

    private void saveSourceCodeToFile(String name, String lang, String source) {
        String ext = "cc";
        if ("c".equalsIgnoreCase(lang)) {
            ext = "c";
        } else if ("pascal".equalsIgnoreCase(lang)) {
            ext = "pas";
        } else if ("java".equalsIgnoreCase(lang)) {
            ext = "java";
        } else if ("python".equalsIgnoreCase(lang)) {
            ext = "py";
        }

        File sourceFile = new File(problem.getDataDir().getAbsolutePath() + File.separator + name + "." + ext);

        try {
            FileUtil.touch(sourceFile);
            FileUtil.writeString(sourceFile, source);
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("write source code to file failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }
    }

    private void saveData() {
        if (problem.getDataIn().size() < 1) {
            saveSampleData();
            return;
        }

        int n = 0;
        Iterator<String> in = problem.getDataIn().iterator();
        Iterator<String> it = problem.getDataOut().iterator();
        for (; in.hasNext() && it.hasNext(); n++) {
            StringBuilder sb = new StringBuilder(6).append(problem.getDataDir().getAbsolutePath());
            sb.append(File.separator).append(problem.getModel().getPid()).append("_").append(n);
            File dataInFile = new File(sb.toString() + OjConstants.DATA_EXT_IN);
            File dataOutFile = new File(sb.toString() + OjConstants.DATA_EXT_OUT);

            String inData = in.next();
            String outData = it.next();

            try {
                FileUtil.touch(dataOutFile);
                FileUtil.writeString(dataOutFile, outData);

                FileUtil.touch(dataInFile);
                FileUtil.writeString(dataInFile, inData);
                log.info(dataInFile.getAbsolutePath());
                log.info(dataOutFile.getAbsolutePath());
            } catch (IOException e) {
                if (OjConfig.isDevMode()) {
                    log.warn("save data file failed!", e);
                }
                log.error(e.getLocalizedMessage());
            }
        }
    }

    private void saveSampleData() {
        File dataInFile =
            new File(problem.getDataDir().getAbsolutePath() + File.separator + "sample" + OjConstants.DATA_EXT_IN);
        File dataOutFile =
            new File(problem.getDataDir().getAbsolutePath() + File.separator + "sample" + OjConstants.DATA_EXT_OUT);

        try {
            FileUtil.touch(dataOutFile);
            FileUtil.writeString(dataOutFile, problem.getModel().getSampleOutput());

            FileUtil.touch(dataInFile);
            FileUtil.writeString(dataInFile, problem.getModel().getSampleInput());
        } catch (IOException e) {
            if (OjConfig.isDevMode()) {
                log.warn("save sample data failed!", e);
            }
            log.error(e.getLocalizedMessage());
        }
    }

    private boolean isInDataFile(File inFile) {
        return inFile.getName().toLowerCase().endsWith(OjConstants.DATA_EXT_IN);
    }

    private String getOutDataFileName(File inFile) {
        return inFile.getName().substring(0, inFile.getName().length() - OjConstants.DATA_EXT_IN.length())
            + OjConstants.DATA_EXT_OUT;
    }

    private String ext2lang(String ext) {
        if (ext.equalsIgnoreCase("c")) {
            return "C";
        } else if (ext.equalsIgnoreCase("cc") || ext.equalsIgnoreCase("cpp")) {
            return "C++";
        } else if (ext.equalsIgnoreCase("pas")) {
            return "Pascal";
        } else if (ext.equalsIgnoreCase("java")) {
            return "Java";
        } else if (ext.equalsIgnoreCase("py")) {
            return "Python";
        }
        return "";
    }

}
