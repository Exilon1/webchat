package servlets;

import util.TemplateEngine;
import dao.AuthentificationCrud;
import dao.MessagesCrud;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
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
        authentificationCrud.close();
    }

    @Override
    public void init() throws ServletException {
        messagesCrud.connect("h2Connection");
        authentificationCrud.connect("h2Connection");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
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
            genChatPage(resp, nickName, session.getServletContext());
        } else {
            System.out.println("chat forward to login");
            req.getRequestDispatcher("/login").forward(req,resp);
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
            genChatPage(resp, nickname, session.getServletContext());
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    private void genChatPage(HttpServletResponse resp, String nickName, ServletContext servletContext) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nick", nickName);
        resp.addCookie(new Cookie("nickname", URLEncoder.encode(nickName, "UTF-8")));
        resp.getWriter().println(TemplateEngine.getInstance().getPage("index.html", pageVariables, servletContext));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().close();
    }

}
