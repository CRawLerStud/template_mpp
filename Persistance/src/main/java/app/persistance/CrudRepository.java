package app.persistance;

import app.persistance.utils.RepositoryException;

import java.util.List;

public interface CrudRepository<ID, T> {

    ID save(T entity) throws RepositoryException;
    T remove(ID id) throws RepositoryException;
    void update(T newEntity) throws RepositoryException;
    T get(ID id) throws RepositoryException;
    List<T> getAll() throws RepositoryException;

}
