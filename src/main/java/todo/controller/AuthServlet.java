package todo.controller;

import todo.model.User;
import todo.store.SqlStore;
import todo.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    private static final Store STORE = SqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String sign = user.getName() + " | <a id=\"reg\" href=\"http://localhost:8080/todo/reg\">Sign out</a>";
        resp.getWriter().write(sign);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = STORE.findUserByEmail(email);
        if (user != null && email.equals(user.getEmail()) && password.equals(user.getPassword())) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect("index.html");
        } else {
            req.getRequestDispatcher("login.html").forward(req, resp);
        }
    }
}
