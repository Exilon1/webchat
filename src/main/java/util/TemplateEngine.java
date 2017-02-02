package util;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * Created by admin on 25.01.2017.
 */
public class TemplateEngine {
    private static final String HTML_DIR = "WEB-INF/templates";


    private static TemplateEngine templateEngine;
    private final Configuration cfg;

    public static TemplateEngine instance(){
        if (templateEngine ==null)
            templateEngine = new TemplateEngine();
        return templateEngine;
    }

    public String getPage(String filename, Map<String,Object> data) throws UnsupportedEncodingException {
        Writer stream = new StringWriter();
        try {
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            FileTemplateLoader templateLoader = new FileTemplateLoader(new File("C:\\Users\\Nikotin\\IdeaProjects\\webchat\\src\\main\\webapp\\WEB-INF\\templates"));
            cfg.setTemplateLoader(templateLoader);

            Template template = cfg.getTemplate(filename,"UTF-8");
            template.process(data,stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }
    private TemplateEngine(){cfg = new Configuration();}
}
