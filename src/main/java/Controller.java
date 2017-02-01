

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Alexey on 25.01.2017.
 */
public class Controller extends HttpServlet {

    private final String PAGECOD_ONE = "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
            "<meta charset=\"utf-8\">" +
            "<link href=\"https://fonts.googleapis.com/css?family=Roboto\" rel=\"stylesheet\">" +
            "<meta http-equiv=\"refresh\" content=\"10; URL=./\">" +
            "<title>HW_WebCalc</title> </head> <body> <div id=\"container\">" +
            "<div id=\"msg_history_div\">" +
            "<ul>";
    private final String PAGECOD_TWO = "</ul>" +
            "</div>" +
            "<form action=\"./\" method=\"post\">" +
            "<label for=\"nick\">Ник пользователя</label>" +
            "<input id=\"nick\" required value=\"";
    private final String PAGECOD_THREE = "\" type=\"text\" name=\"nick\" maxlength=\"50\">" +
            "<label for=\"msg\">Сообщение</label>" +
            "<textarea id=\"msg\" required rows=\"5\" type=\"textarea\" name=\"msg\" maxlength=\"200\"></textarea>" +
            "<input type=\"submit\" value=\"Отправить\">" +
            "</form>" +
            "</div>" +
            "<style>" +
            "#container{ font-family: 'Roboto', sans-serif; font-size:16px; padding: 8px; border: 1px solid #dfdfdf; color: #3f3f3f; width: 360px; margin: 150px auto; }" +
            "#msg_history_div{ height: 160px; overflow: auto; border: 1px solid #dfdfdf; }" +
            "#msg_history_div ul{ list-style: none; margin: 8px; padding: 0; }" +
            "#container form{ margin: 8px 0 8px 0; }" +
            "input, textarea { width: 100%; }" +
            "label { padding-bottom:4px; padding-top:4px; display: block; }" +
            "input[type=\"submit\"]{ margin-top:8px; }" +
            "</style>" +
            "</body>" +
            "</html>";

    private JdbcCrud jdbcCrud = JdbcCrud.getInstance();
    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    public void init() throws ServletException {
        jdbcCrud.connect("h2Connection");
    }

    @Override
    public void destroy() {
        jdbcCrud.close();
    }

    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        req.getSession();
        String nickName = "";
        String JSESSIONID = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nickname".equals(cookie.getName())) {
                    nickName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }
        if ("/".equals(path)) {
            String message = req.getParameter("msg");
            String nickname = req.getParameter("nick");
            Date date = new Date();
            if (message != null && nickname != null) {
                jdbcCrud.insert(nickname, date, message);
                genChatPage(resp, nickname);
            } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void genChatPage(HttpServletResponse resp, String nickName) throws IOException {
        Map<String, Object> pageVariables = new HashMap<String, Object>();
        pageVariables.put("nick", nickName);
        pageVariables.put("messages", jdbcCrud.read());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.addCookie(new Cookie("nickname", URLEncoder.encode(nickName, "UTF-8")));
        resp.getWriter().println(new String(templateEngine.getPage("index.html", pageVariables)));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().close();
    }
}