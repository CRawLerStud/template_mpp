package app.persistance.implementation;

import app.model.Game;
import app.model.Move;
import app.persistance.MoveRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

public class HibernateMoveRepository extends AbstractCrudRepository<Long, Move> implements MoveRepository {

    public HibernateMoveRepository() {
        this.entityClass = Move.class;
    }

    @Override
    public Integer getSizeForGame(Long gameID) throws RepositoryException{
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            Game game = session.get(Game.class, gameID);
            Integer size = game.getMoves().size();
            session.getTransaction().commit();
            return size;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }
}
