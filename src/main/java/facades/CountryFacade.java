package facades;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brandstrup
 */
public class CountryFacade {

    private static CountryFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private CountryFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CountryFacade getCountryFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CountryFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    //TODO Remove/Change this before use
    public long getCountryCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long countryCount = (long)em.createQuery("SELECT COUNT(c) FROM Country c").getSingleResult();
            return countryCount;
        }finally{  
            em.close();
        }
        
    }

}
