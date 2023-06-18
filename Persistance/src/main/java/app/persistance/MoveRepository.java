package app.persistance;

import app.model.Move;
import app.persistance.implementation.AbstractCrudRepository;
import app.persistance.utils.RepositoryException;

public interface MoveRepository extends CrudRepository<Long, Move> {
    Integer getSizeForGame(Long id) throws RepositoryException;
}
