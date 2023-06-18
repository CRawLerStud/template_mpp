package app.persistance;

import app.model.User;
import app.persistance.utils.RepositoryException;

public interface UserRepository extends CrudRepository<Long, User> {

    User checkCredentials(String username, String password) throws RepositoryException;

}
