package servlets;

import dao.AuthentificationCrud;

import javax.servlet.ServletException;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        authentificationCrud.close();
    }

    @Override
    public void init() throws ServletException {
        authentificationCrud.connect("h2Connection");
    }
}
