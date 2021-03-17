package todo.store;

import todo.model.Item;
import todo.model.User;

import java.util.List;

public interface Store {

    void addItem(Item item);

    List<Item> findAllItems();

    List<Item> findAllUndone();

    void checkItem(int done, int id);

    void addUser(User user);

    User findUserByEmail(String email);
}
