package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todo.model.Item;

import javax.persistence.Query;
import java.util.List;

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
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Item> findAllItems() {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session.createQuery("from todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    @Override
    public List<Item> findAllUndone() {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from todo.model.Item where done = 0");
        List items = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    @Override
    public void checkItem(int done, int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery(
                "update todo.model.Item set done = :done where id = :id"
        );
        query.setParameter("done", done);
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
