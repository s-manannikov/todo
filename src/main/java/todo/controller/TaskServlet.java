package todo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import todo.model.Item;
import todo.model.User;
import todo.store.SqlStore;
import todo.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private static final Store STORE = SqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        String json = req.getParameter("json");
        List<Item> items = new ArrayList<>();
        if (json.equals("0")) {
            items = STORE.findAllUndone();
        }
        if (json.equals("1")) {
            items = STORE.findAllItems();
        }
        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().write(mapper.writeValueAsString(items));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(req.getReader());
        String description = jsonNode.get("description").asText();
        List<String> categories = Arrays.asList(objectMapper.treeToValue(jsonNode.get("category"), String[].class));
        Item item = new Item();
        item.setDescription(description);
        item.setCreated(new Date(System.currentTimeMillis()));
        item.setUser(user);
        STORE.addItem(item, categories);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
