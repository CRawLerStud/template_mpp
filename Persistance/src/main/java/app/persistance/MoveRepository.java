package app.persistance;

import app.model.Move;
import app.persistance.implementation.AbstractCrudRepository;

public interface MoveRepository extends CrudRepository<Long, Move> {
}
