package app.persistance.implementation;

import app.model.Configuration;
import app.persistance.ConfigurationRepository;
import app.persistance.utils.HibernateSession;
import app.persistance.utils.RepositoryException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class HibernateConfigurationRepository extends AbstractCrudRepository<Long, Configuration> implements ConfigurationRepository {

    public HibernateConfigurationRepository() {
        this.entityClass = Configuration.class;
    }

    @Override
    public Configuration getRandomConfiguration() throws RepositoryException {
        try(Session session = HibernateSession.getInstance().openSession()){
            session.beginTransaction();
            List<Configuration> configurationList =
                    session.
                            createQuery("FROM Configuration ")
                            .getResultList();
            Random random = new Random();
            int randomIndex = random.nextInt(configurationList.size());
            session.getTransaction().commit();
            return configurationList.get(randomIndex);
        }
        catch(Exception ex){
            throw new RepositoryException(ex.getMessage());
        }
    }
}
