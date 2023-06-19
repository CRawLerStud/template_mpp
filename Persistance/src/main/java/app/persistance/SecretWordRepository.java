package app.persistance;

import app.model.SecretWord;
import app.persistance.utils.RepositoryException;

import java.util.List;

public interface SecretWordRepository extends CrudRepository<Long, SecretWord> {

    List<SecretWord> secretWordsForGame(Long gameID) throws RepositoryException;
}
