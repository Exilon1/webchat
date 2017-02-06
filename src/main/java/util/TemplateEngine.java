package util;

import dao.MessagesCrud;
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

    private static final String HTML_DIR = "templates";
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

    public String getPage(String filename, Map<String,Object> data) throws UnsupportedEncodingException {
        Writer stream = new StringWriter();
        try {
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename, "UTF-8");
            template.process(data,stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

}
