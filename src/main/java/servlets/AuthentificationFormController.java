package servlets;

import dao.AuthentificationCrud;
import util.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * Created by Nikotin on 02.02.2017.
 */
public class AuthentificationFormController extends HttpServlet {

    private AuthentificationCrud authentificationCrud = AuthentificationCrud.getInstance();
    private final String SESSION = "JSESSIONID";

    @Override
    public void destroy() {
    //    authentificationCrud.close();
        System.out.println("AuthentificationFormController destroy");
    }

    @Override
    public void init() throws ServletException {
    //    authentificationCrud.connect("h2Connection");
        System.out.println("AuthentificationFormController init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String jsessionid = null;
        for (Cookie cookie : req.getCookies()) {
            if (SESSION.equals(cookie.getName())) {
                jsessionid = cookie.getValue();
            }
        }
        if (authentificationCrud.isSessionContains(jsessionid)) {
            System.out.println("AuthentificationFormController isSessionContains true");
            resp.sendRedirect("./");
        } else {
            System.out.println("AuthentificationFormController isSessionContains false");
            genPage(resp, session.getServletContext());
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        if (login != null & pass != null) {
            boolean isAuth = authentificationCrud.verifyAccount(login, pass);
                        if (isAuth) {
                String jsessionid = null;
                for (Cookie cookie: req.getCookies()){
                    if (SESSION.equals(cookie.getName())){
                        jsessionid = cookie.getValue();
                        break;
                    }
                }
                if (jsessionid!=null)
                    authentificationCrud.insertSession(jsessionid, login);
                resp.addCookie(new Cookie("nickname", URLEncoder.encode(login, "UTF-8")));
                resp.sendRedirect("./");
            } else genPage(resp, session.getServletContext());
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }


    private void genPage(HttpServletResponse resp, ServletContext servletContext) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.println(TemplateEngine.getInstance().generatePage("login.html", null, servletContext));
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        writer.close();
    }
}
