import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by Alexey on 25.01.2017.
 */
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String str = "";
        if (cookies != null)
        {
            for(int i=0; i<cookies.length; i++)
            {
                Cookie cookie = cookies[i];
                str = str + cookie.getValue() + ", ";
            }
        }
        resp.setContentType( "text/html" );
 /*       String text = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.01 Transitional//EN\" " +
                "\"http://www.w3.org/TR/html4/loose.dtd\"> " +
                "<html><head>" +
                "<meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=UTF-8\"> " +
                "<title>Пример сервлета!</title>" +
                "</head>" +
                "<body>" +
                "<h1>Здравствуй, " + str + "</h1>" +
                "</body></html>";

        OutputStream outStream = null;
        try {
            outStream = resp.getOutputStream();
            outStream.write(text.getBytes("UTF-8"));
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Enumeration en = req.getParameterNames();
        while(en.hasMoreElements()) {
            // Get the name of the request parameter
            String name = (String)en.nextElement();
            out.println(name);

            // Get the value of the request parameter
            String value = req.getParameter(name);

            // If the request parameter can appear more than once in the query string, get all values
            String[] values = req.getParameterValues(name);

            for (int i=0; i<values.length; i++) {
                out.println(" " + values[i]);
            }
        }
    }
}