package servlets;

import util.TemplateEngine;
import dao.AuthentificationCrud;
import dao.MessagesCrud;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 24.01.2017.
 */
public class ChatWindowController extends HttpServlet {

    private MessagesCrud messagesCrud = MessagesCrud.getInstance();
    private AuthentificationCrud authentificationCrud = AuthentificationCrud.getInstance();

    private final String SESSION = "JSESSIONID";

    @Override
    public void destroy() {
        messagesCrud.close();
        authentificationCrud.close();
        System.out.println("ChatWindowController destroy");
    }

    @Override
    public void init() throws ServletException {
        messagesCrud.connect("h2Connection");
        authentificationCrud.connect("h2Connection");
        System.out.println("ChatWindowController init");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String nickName = null;
        String jsessionid = null;
        for (Cookie cookie : req.getCookies()) {
            if ("nickname".equals(cookie.getName())) {
                nickName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                System.out.println(">>>>>>> " + cookie.getValue());
            }
            if (SESSION.equals(cookie.getName())) {
                jsessionid = cookie.getValue();
            }
        }

    //    System.out.println(jsessionid);
        if (authentificationCrud.isSessionContains(jsessionid)) {
            genPage(resp, nickName, session.getServletContext());
        } else {
            System.out.println("chat forward to login");
            resp.sendRedirect("./login");
        }
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String msg = req.getParameter("msg");
        String nickname = req.getParameter("nick");
        Date msgDate = new Date();
        if (msg != null & nickname != null) {
            messagesCrud.insert(nickname, msgDate, msg);
            genPage(resp, nickname, session.getServletContext());
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    private void genPage(HttpServletResponse resp, String nickName, ServletContext servletContext) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("nick", nickName);
        resp.addCookie(new Cookie("nickname", URLEncoder.encode(nickName, "UTF-8")));
        PrintWriter writer = resp.getWriter();
        writer.println(TemplateEngine.getInstance().generatePage("index.html", map, servletContext));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        writer.close();
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
    }

}
