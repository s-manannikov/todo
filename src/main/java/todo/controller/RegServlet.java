package todo.controller;

import todo.model.User;
import todo.store.SqlStore;
import todo.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reg")
public class RegServlet extends HttpServlet {
    private static final Store STORE = SqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect("login.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        STORE.addUser(user);
        resp.sendRedirect("login.html");
    }
}
