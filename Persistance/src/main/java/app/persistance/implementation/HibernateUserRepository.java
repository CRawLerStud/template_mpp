package app.persistance.implementation;

import app.model.User;
import app.persistance.UserRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;

public class HibernateUserRepository extends AbstractCrudRepository<Long, User> implements UserRepository {

    public HibernateUserRepository() {
        this.entityClass = User.class;
    }

    @Override
    public User checkCredentials(String username, String password) throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            User user = (User) session
                    .createQuery("FROM User WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            session.getTransaction().commit();
            return user;
        }
        catch(Exception exception){
            throw new RepositoryException(exception.getMessage());
        }
    }
}
