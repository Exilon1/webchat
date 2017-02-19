package servlets;

import util.CookieHelper;
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
        String nickName = URLDecoder.decode(CookieHelper.getCookieName(req.getCookies(), "nickname"), "UTF-8");
        String jsessionid = CookieHelper.getCookieName(req.getCookies(), SESSION);

    //    System.out.println(jsessionid);
        if (authentificationCrud.verifySession(jsessionid)) {
            genPage(resp, nickName, session.getServletContext());
        } else {
            resp.sendRedirect("./login");
        }
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    private String name = null;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String msg = req.getParameter("msg");
        String nickname = req.getParameter("nick");
        Date msgDate = new Date();
        if (msg != null & nickname != null) {
            if(!authentificationCrud.isNicknameContains(nickname))
                resp.sendRedirect("./login");;
            messagesCrud.insert(nickname, msgDate, msg);
            genPage(resp, nickname, session.getServletContext());
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    private void genPage(HttpServletResponse resp, String nickName, ServletContext servletContext) throws IOException {
        Map<String, Object> map = new HashMap<>();
        resp.setContentType("text/html;charset=utf-8");
        resp.addCookie(new Cookie("nickname", URLEncoder.encode(nickName, "UTF-8")));
        PrintWriter writer = resp.getWriter();
        map.put("messages", messagesCrud.read());
        map.put("nick", nickName);
        writer.println(TemplateEngine.getInstance().generatePage("index.html", map, servletContext));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        writer.close();
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
    }

}
