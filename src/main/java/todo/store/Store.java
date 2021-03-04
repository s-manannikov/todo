package todo.store;

import todo.model.Item;

import java.util.List;

public interface Store {

    void addItem(Item item);

    List<Item> findAllItems();

    List<Item> findAllUndone();

    void checkItem(int done, int id);
}
