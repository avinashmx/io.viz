//package com.gent00.datapoint.generator;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateExceptionHandler;
//import freemarker.template.Version;
//import freemarker.template.utility.StringUtil;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class ScenarioGenerator {
//    private List<String> blockSizes;
//    private File[] files;
//    private Scenario scenario;
//
//    public ScenarioGenerator(Scenario scenario,List<String> blockSizes, File[] files) {
//        this.blockSizes = blockSizes;
//        this.files = files;
//        this.scenario = scenario;
//    }
//
//    public void execute() throws Exception {
//        Configuration cfg = new Configuration();
//
//        // Where do we load the templates from:
//        cfg.setClassForTemplateLoading(ScenarioGenerator.class, "/templates");//Absolute classpath root
//
//        // Some other recommended settings:
//        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
//        cfg.setDefaultEncoding("UTF-8");
//        cfg.setLocale(Locale.US);
//        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//
//        Template template = cfg.getTemplate("random_sequential_write.ftl");
//
//        Map<String, Object> input = new HashMap<String, Object>();
//        input.put("title", "Vogella example");
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Writer consoleWriter = new OutputStreamWriter(baos);
//        template.p1rocess(input, consoleWriter);
//
//        String renderedTemplate = baos.toString();
//        BufferedReader reader = new BufferedReader(new StringReader(renderedTemplate));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (!line.startsWith("#") && !line.isEmpty()) {
//                System.out.println(line);
//            }
//        }
//
//
//
//
//
//
//    }
//
//    public enum Scenario {SINGLE_DRIVE, MULTIPLE_DRIVE,}
//}
