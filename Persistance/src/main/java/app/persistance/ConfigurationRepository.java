package app.persistance;

import app.model.Configuration;
import app.persistance.utils.RepositoryException;

public interface ConfigurationRepository extends CrudRepository<Long, Configuration> {

    Configuration getRandomConfiguration() throws RepositoryException;

}
