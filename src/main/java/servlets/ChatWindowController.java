package servlets;

import util.TemplateEngine;
import dao.AuthentificationCrud;
import dao.MessagesCrud;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Override
    public void destroy() {
        messagesCrud.close();
    }

    @Override
    public void init() throws ServletException {
        messagesCrud.connect("h2Connection");

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                if ("JSESSIONID".equals(cookie.getName())) {
                    JSESSIONID = cookie.getValue();
                }
            }
        }
        if (authentificationCrud.isSessionContains(JSESSIONID)) {
            genChatPage(resp, nickName);
        } else {
        //    genLoginPage(resp);
            resp.sendRedirect("/login");
        }
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        req.getSession();
        if ("/login".equals(path)) {
            String login = req.getParameter("login");
            String pass = req.getParameter("pass");
            if (login != null & pass != null) {
                boolean isAuth = authentificationCrud.verifyAccount(login, pass);
                if (isAuth) {
                    Cookie[] cookies = req.getCookies();
                    if (cookies!=null)
                        for (Cookie cookie: cookies){
                        if ("JSESSIONID".equals(cookie.getName())){
                            authentificationCrud.insertSession(cookie.getValue());
                            break;
                        }
                    }
                    genChatPage(resp, login);
                }
                else genLoginPage(resp);
            } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if ("/".equals(path)) {
            String msg = req.getParameter("msg");
            String nickname = req.getParameter("nick");
            Date msgDate = new Date();
            if (msg != null & nickname != null) {
                messagesCrud.insert(nickname, msgDate, msg);
                genChatPage(resp, nickname);
            } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);


    }

    private void genLoginPage(HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(TemplateEngine.instance().getPage("login.html", null));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().close();

    }

    private void genChatPage(HttpServletResponse resp, String nickName) throws IOException {
        Map<String, Object> pageVariables = new HashMap<String, Object>();
        pageVariables.put("nick", nickName);
        pageVariables.put("messages", messagesCrud.read());
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.addCookie(new Cookie("nickname", URLEncoder.encode(nickName, "UTF-8")));
        resp.getWriter().println(TemplateEngine.instance().getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().close();
    }

}
