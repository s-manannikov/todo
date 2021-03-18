package todo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(req.getReader());
        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        STORE.addUser(user);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
