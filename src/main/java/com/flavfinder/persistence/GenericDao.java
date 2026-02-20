package com.flavfinder.persistence;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

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
    public <T>T getById(int id) {
        Session session = getSession();
        T entity = (T)session.get(type, id);
        session.close();
        return entity;
    }

    /**
     * update entity
     *
     * @param entity entity to be updated
     */
    public void update(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(entity);
        transaction.commit();
        session.close();
    }

    /**
     * insert a new entity
     *
     * @param entity entity to be inserted
     */
    public int insert(T entity) {
        int id = 0;
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        // Persist - save the "entity"
        session.persist(entity);
        transaction.commit();
        id = (int) session.getIdentifier(entity);
        session.close();
        return id;
    }

    /**
     * Deletes the entity
     *
     * @param entity entity to be deleted
     */
    public void delete(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
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

        log.debug("The list of entitys " + list);
        session.close();

        return list;
    }
}
