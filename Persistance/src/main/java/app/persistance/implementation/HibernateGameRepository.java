package app.persistance.implementation;

import app.model.Game;
import app.persistance.GameRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateGameRepository extends AbstractCrudRepository<Long, Game> implements GameRepository {

    public HibernateGameRepository() {
        this.entityClass = Game.class;
    }

    @Override
    public List<Game> getAllGamesForPlayer(Long playerID) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<Game> games =
                    session
                            .createQuery("FROM Game G " +
                                    "WHERE G.user.id = :userID")
                            .setParameter("userID", playerID)
                            .getResultList();
            session.getTransaction().commit();
            return games;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Game> getAllGamesForConfiguration(Long configurationID) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<Game> games =
                    session.createQuery("FROM Game G WHERE G.configuration.id = :configurationID AND G.finished = true")
                            .setParameter("configurationID", configurationID)
                            .getResultList();
            session.getTransaction().commit();
            return games;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }
}
