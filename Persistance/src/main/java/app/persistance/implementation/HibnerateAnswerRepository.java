package app.persistance.implementation;

import app.model.Answer;
import app.persistance.AnswerRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibnerateAnswerRepository extends AbstractCrudRepository<Long, Answer> implements AnswerRepository {

    public HibnerateAnswerRepository() {
        this.entityClass = Answer.class;
    }

    @Override
    public List<Answer> getAnswersForGameAndPlayer(Long gameID, Long userID) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<Answer> answers =
                    session.createQuery("FROM Answer A " +
                            "WHERE A.round.game.id = :gameID AND A.user.id = :userID")
                            .setParameter("gameID", gameID)
                            .setParameter("userID", userID)
                            .getResultList();
            session.getTransaction().commit();
            return answers;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }
}
