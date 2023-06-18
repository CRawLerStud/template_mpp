package app.persistance;

import app.model.Game;
import app.persistance.utils.RepositoryException;

import java.util.List;

public interface GameRepository extends CrudRepository<Long, Game>{

    List<Game> getAllGamesForPlayer(Long playerID) throws RepositoryException;

}
