package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todo.model.Item;

import javax.persistence.Query;
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
    public void addItem(Item item) {
        transaction(session -> session.save(item));
    }

    @Override
    public List<Item> findAllItems() {
        return transaction(session -> session.createQuery("from todo.model.Item").list());
    }

    @Override
    public List<Item> findAllUndone() {
        return transaction(session -> session.createQuery("from todo.model.Item where done = 0").list());
    }

    @Override
    public void checkItem(int done, int id) {
        transaction(session -> {
            final Query query = session.createQuery("update todo.model.Item set done = :done where id = :id");
            query.setParameter("done", done);
            query.setParameter("id", id);
            return query.executeUpdate();
        });
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
