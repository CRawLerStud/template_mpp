package app.persistance;

import app.model.Answer;
import app.persistance.utils.RepositoryException;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Long, Answer> {

    List<Answer> getAnswersForGameAndPlayer(Long gameID, Long playerID) throws RepositoryException;

}
