package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import todo.model.Category;
import todo.model.Item;
import todo.model.User;

import java.util.List;
import java.util.function.Function;

public class SqlStore implements AutoCloseable, Store {
    private static final Store INST = new SqlStore();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private SqlStore() {
    }

    public static Store instOf() {
        return INST;
    }

    @Override
    public void addItem(Item item, List<String> list) {
        transaction(session -> {
            for (String s : list) {
                Category category = session.find(Category.class, Integer.parseInt(s));
                item.addCategory(category);
            }
            return session.save(item);
        });
    }

    @Override
    public List<Item> findAllItems() {
        return transaction(session -> session.createQuery(
                "select distinct i from Item i join fetch i.categories"
        ).list());
    }

    @Override
    public List<Item> findAllUndone() {
        return transaction(session -> session.createQuery(
                "select distinct i from Item i join fetch i.categories where i.done = 0"
        ).list());
    }

    @Override
    public void checkItem(int done, int id) {
        transaction(session -> {
            final Query query = session.createQuery("update Item set done = :done where id = :id");
            query.setParameter("done", done);
            query.setParameter("id", id);
            return query.executeUpdate();
        });
    }

    public void addUser(User user) {
        transaction(session -> session.save(user));
    }

    public User findUserByEmail(String email) {
        return transaction(session -> {
            final Query query = session.createQuery("from User where email = :email");
            query.setParameter("email", email);
            return (User) query.uniqueResult();
        });
    }

    public List<Category> getAllCategories() {
        return transaction(session -> session.createQuery("from Category").list());
    }

    private <T> T transaction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        session.beginTransaction();
        try {
            T rsl = command.apply(session);
            session.getTransaction().commit();
            return rsl;
        } catch (final Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
