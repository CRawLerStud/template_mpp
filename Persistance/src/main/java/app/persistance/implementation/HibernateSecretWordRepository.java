package app.persistance.implementation;

import app.model.SecretWord;
import app.persistance.SecretWordRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateSecretWordRepository extends AbstractCrudRepository<Long, SecretWord> implements SecretWordRepository {

    public HibernateSecretWordRepository() {
        this.entityClass = SecretWord.class;
    }

    @Override
    public List<SecretWord> secretWordsForGame(Long gameID) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<SecretWord> words =
                    session.createQuery("FROM SecretWord S WHERE S.game.id = :gameID")
                            .setParameter("gameID", gameID)
                            .getResultList();
            session.getTransaction().commit();
            return words;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<SecretWord> getSecretWordsForUser(Long userID, Long gameID) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<SecretWord> words =
                    session.createQuery("FROM SecretWord S WHERE S.user.id != :userID AND S.game.id = :gameID")
                            .setParameter("userID", userID)
                            .setParameter("gameID", gameID)
                            .getResultList();
            session.getTransaction().commit();
            return words;
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }
}
