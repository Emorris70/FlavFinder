package com.flavfinder.persistence;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;
import java.util.function.Function;

/**
 * A generic Data Access Object providing common CRUD operations for
 * all entities.
 *
 * @param <T> The type of entity managed by this DAO.
 * @author EmileM
 */
public class GenericDao<T> {
    private static final Logger log = LogManager.getLogger(GenericDao.class);
    private final Class<T> type;

    /**
     * Instantiates a new Generic dao.
     *
     * @param type the entity type, for example, T.
     */
    public GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Returns an open session from the SessionFactory
     *
     * @return the open session
     */
    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

    /**
     * Gets the entity by id
     *
     * @param id entity id to search by
     * @return an entity
     */
    public <T> T getById(int id) {
        Session session = getSession();
        T entity = (T)session.get(type, id);
        session.close();
        return entity;
    }

    /**
     * Insert a new entity and returns an id
     * corresponding to new entry row.
     *
     * @param entity entity to be inserted
     */
    public int insert(T entity) {
        return executeWithSession(session -> {
            session.persist(entity);
            return (int) session.getIdentifier(entity);
        });
    }

    /**
     * Update the entity and returns null.
     *
     * @param entity entity to be updated
     */
    public void update(T entity) {
        executeWithSession(session -> {
            session.merge(entity);
            return null;
        });
    }

    /**
     * Deletes the entity and returns null.
     *
     * @param entity entity to be deleted
     */
    public void delete(T entity) {
        executeWithSession(session -> {
            session.delete(entity);
           return null;
        });
    }

    /**
     * Return a list of all entities
     *
     * @return All entities
     */
    public List<T> getAll() {

        Session session = getSession();

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        List<T> list = session.createSelectionQuery( query ).getResultList();

        log.info("The list of entitys " + list);
        session.close();

        return list;
    }

    /**
     * Wraps a database operation within a Hibernate session and transaction.
     * This method ensures that the transaction is committed on success,
     * rolled back on failure, and the session is always closed.
     *
     * @param action the query to perform
     * @param <R> the return type produced by the action.
     * @return the result in which the calling method would like to return.
     */
    private <R> R executeWithSession(Function<Session, R> action) {
        Session session = getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            R result = action.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
