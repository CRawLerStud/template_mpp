package app.persistance.implementation;

import app.model.Move;
import app.persistance.MoveRepository;

public class HibernateMoveRepository extends AbstractCrudRepository<Long, Move> implements MoveRepository {

    public HibernateMoveRepository() {
        this.entityClass = Move.class;
    }
}
