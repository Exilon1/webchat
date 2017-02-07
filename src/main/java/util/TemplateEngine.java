package util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Map;

/**
 * Created by admin on 25.01.2017.
 */
public class TemplateEngine {

    private final Configuration cfg;


    private TemplateEngine() {
        cfg = new Configuration();
    }

    private static class SingletonHelper {
        private static final TemplateEngine SINGLETON = new TemplateEngine();
    }

    public static TemplateEngine getInstance() {
        return TemplateEngine.SingletonHelper.SINGLETON;
    }

    public String generatePage(String filename, Map<String,Object> data, ServletContext servletContext) throws UnsupportedEncodingException {
        Writer stream = new StringWriter();
        try {
            cfg.setServletContextForTemplateLoading(servletContext, "/");
            Template template = cfg.getTemplate("templates" + File.separator + filename, "UTF-8");
            template.process(data,stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

}
