package app.persistance.implementation;

import app.model.Round;
import app.persistance.RoundRepository;

public class HibernateRoundRepository extends AbstractCrudRepository<Long, Round> implements RoundRepository {

    public HibernateRoundRepository() {
        this.entityClass = Round.class;
    }
}
