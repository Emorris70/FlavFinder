package com.flavfinder.persistence;

import com.flavfinder.entity.User;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

/**
 *
 */
public class UserDao {

    private final Logger logger = LogManager.getLogger(this.getClass());
    // Gets the current db connection
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Get user by id
     */
    public User getById(int id) {
        // opens that current connection to then; in this case GET the user by ID
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);
        // close that connection.
        session.close();
        return user;
    }

    /**
     * update user
     *
     * @param user User to be updated
     */
    public void update(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(user);
        transaction.commit();
        session.close();
    }

    /**
     * insert a new user
     *
     * @param user User to be inserted
     */
    public int insert(User user) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        // Persist - save the "user"
        session.persist(user);
        transaction.commit();
        id = user.getId();
        session.close();
        return id;
    }

    /**
     * Delete a user
     *
     * @param user User to be deleted
     */
    public void delete(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }


    /**
     * Return a list of all users
     *
     * @return All users
     */
    public List<User> getAll() {

        Session session = sessionFactory.openSession();

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        List<User> users = session.createSelectionQuery(query).getResultList();

        logger.debug("The list of users " + users);
        session.close();

        return users;
    }
}
