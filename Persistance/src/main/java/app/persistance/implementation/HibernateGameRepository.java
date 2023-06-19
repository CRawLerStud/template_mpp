package app.persistance.implementation;

import app.model.Game;
import app.persistance.GameRepository;
import org.springframework.stereotype.Component;

@Component
public class HibernateGameRepository extends AbstractCrudRepository<Long, Game> implements GameRepository {

    public HibernateGameRepository() {
        this.entityClass = Game.class;
    }
}
