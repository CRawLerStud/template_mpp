package app.persistance.implementation;

import app.persistance.CrudRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public class AbstractCrudRepository<ID, T> implements CrudRepository<ID, T> {

    private final static Logger log= LogManager.getLogger();

    protected Class<T> entityClass;

    public AbstractCrudRepository() {}

    @Override
    public ID save(T entity) throws RepositoryException {
        log.traceEntry("Saving " + entityClass.getSimpleName() + ": " + entity);
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            ID id = (ID) session.save(entity);
            session.getTransaction().commit();
            log.traceExit("Returning the new id: " + id);
            return id;
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public T remove(ID id) throws RepositoryException {
        log.traceEntry("Removing " + entityClass.getSimpleName() + " with id: " + id);
        try(Session session = HibernateSession.getInstance().openSession()) {
            session.beginTransaction();
            T entity = session.get(entityClass, id);
            session.remove(entity);
            session.getTransaction().commit();
            log.traceExit("Returning the entity: " + entity);
            return entity;
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void update(T newEntity) throws RepositoryException {
        log.traceEntry("Updating " + entityClass.getSimpleName() + " with newEntity: " + newEntity);
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            session.merge(newEntity);
            log.traceExit(entityClass.getSimpleName() + " updated!");
            session.getTransaction().commit();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public T get(ID id) throws RepositoryException {
        log.traceEntry("Finding " + entityClass.getSimpleName() + " with id: " + id);
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            T entity = session.get(entityClass, id);
            session.getTransaction().commit();
            log.traceExit("Returning entity: " + entity);
            return entity;
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<T> getAll() throws RepositoryException{
        log.traceEntry("Returning all " + entityClass.getSimpleName() + "s");
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            TypedQuery<T> typedQuery = session.createQuery(criteriaQuery);
            List<T> allEntities = typedQuery.getResultList();
            session.getTransaction().commit();
            log.traceExit(entityClass.getSimpleName() + "s returned!");
            return allEntities;
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            throw new RepositoryException(ex.getMessage());
        }
    }
}
