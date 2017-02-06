package servlets;

import dao.AuthentificationCrud;
import util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nikotin on 02.02.2017.
 */
public class AuthentificationFormController extends HttpServlet {

    private AuthentificationCrud authentificationCrud = AuthentificationCrud.getInstance();

    @Override
    public void destroy() {
        authentificationCrud.close();
    }

    @Override
    public void init() throws ServletException {
        authentificationCrud.connect("h2Connection");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession();
        String JSESSIONID = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    JSESSIONID = cookie.getValue();
                }
            }
        }
        if (authentificationCrud.isSessionContains(JSESSIONID)) {
            resp.sendRedirect("./");
        } else {
            genLoginPage(resp);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("Nickname");
        String pass = req.getParameter("Password");
        if (login != null & pass != null) {
            boolean isAuth = authentificationCrud.verifyAccount(login, pass);
            if (isAuth) {
                String jsessionid = "";
                Cookie[] cookies = req.getCookies();
                if (cookies!=null)for (Cookie cookie:cookies){
                    if ("JSESSIONID".equals(cookie.getName())){
                        jsessionid = cookie.getValue();
                        break;
                    }
                }
                authentificationCrud.isSessionContains(jsessionid);
                resp.sendRedirect("./");
            } else genLoginPage(resp);
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }


    private void genLoginPage(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(TemplateEngine.getInstance().getPage("login.html", null));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().close();

    }
}
