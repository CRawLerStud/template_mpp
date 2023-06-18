package app.persistance.implementation;

import app.model.Configuration;
import app.persistance.ConfigurationRepository;
import org.springframework.stereotype.Component;

@Component
public class HibernateConfigurationRepository extends AbstractCrudRepository<Long, Configuration> implements ConfigurationRepository {

    public HibernateConfigurationRepository() {
        this.entityClass = Configuration.class;
    }
}
