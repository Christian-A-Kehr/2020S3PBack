/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.CountryBasicInDTO;
import dtos.CountryExDTO;
import dtos.CountryInDTO;
import dtos.CovidExDTO;
import entities.CountryData;
import entities.CovidData;
import utils.EMF_Creator;
import entities.RenameMe;
import errorhandling.DatabaseException;
import errorhandling.NotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

/**
 *
 * @author Christian
 */
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class CountryFacadeTest
{

    private static EntityManagerFactory emf;
    private static CountryFacade facade;
    CountryData cd1 = new CountryData("LandOfTheBrave", "XX", 100, null, null);
    CountryData cd2 = new CountryData("LalaLand", "YY", 2, null, null);
    CountryExDTO DTO1 = new CountryExDTO("ZombieLand", "ZL", 4);

    public CountryFacadeTest()
    {
    }

    //@BeforeAll
    public static void setUpClass()
    {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/2020S3P_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = CountryFacade.getCountryFacade(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2()
    {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CountryFacade.getCountryFacade(emf);
    }

    @AfterAll
    public static void tearDownClass()
    {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp()
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            em.createNamedQuery("CountryData.deleteAllRows").executeUpdate();
            em.persist(cd1);
            em.persist(cd2);
            em.getTransaction().commit();
        } finally
        {
            em.close();
        }
    }

    @AfterEach
    public void tearDown()
    {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testGetInternalCountryCount()
    {
        long ex = 2;
        long count = facade.getInternalCountryCount();
        assertEquals(ex, count, "Expects two rows in the database");
    }
    /**
     * author Christian
     * @throws NotFoundException 
     */
    @Test
    public void testPersistExternalCountry() throws NotFoundException
    {
        try
        {
            String expt = facade.persistExternalCountry(DTO1).getCountryName();
            String CCode = DTO1.getAlpha2Code();
            String res = facade.getInternalCountryByCode(CCode).getCountryName();
            System.out.println("persisteExCountryTest: expt =" + expt + " res= " + res);
            assertEquals(expt, res, "Expects two rows in the database");
        } catch (DatabaseException ex)
        {
            Logger.getLogger(CountryFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * author Christian
     * @throws NotFoundException 
     */
    @Test
    public void testGetInternalCountryByCode() throws NotFoundException
    {
        String expt = cd2.getCountryName();
        String lookingForCC = cd2.getCountryCode();
        String res = facade.getInternalCountryByCode(lookingForCC).getCountryName();
        System.out.println("getCountryFromDatabaseByCountrycode: expt =" + expt + " res= " + res);
        assertEquals(expt, res, "Expects LalaLand");
    }

//    /**
//     * Test of getAllInternalCountries method, of class CountryFacade.
//     */
//    @Test
//    public void testGetAllInternalCountries() throws Exception
//    {
//        System.out.println("getAllInternalCountries");
//        CountryFacade instance = null;
//        List<CountryBasicInDTO> expResult = null;
//        List<CountryBasicInDTO> result = instance.getAllInternalCountries();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of getNewestInternalCovidEntryForCountryByCode method, of class CountryFacade.
//     */
//    @Test
//    public void testGetNewestInternalCovidEntryForCountryByCode()
//    {
//        String code = "";
//        CountryFacade instance = null;
//        CountryInDTO expResult = null;
//        CountryInDTO result = instance.getNewestInternalCovidEntryForCountryByCode(code);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of getAllInternalCovidEntriesForCountryByCode method, of class CountryFacade.
//     */
//    @Test
//    public void testGetAllInternalCovidEntriesForCountryByCode()
//    {
//        System.out.println("getAllInternalCovidEntriesForCountryByCode");
//        CountryFacade instance = null;
//        instance.getAllInternalCovidEntriesForCountryByCode();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of getInternalCovidEntryForCountryByCodeByDate method, of class CountryFacade.
//     */
//    @Test
//    public void getMultipleInternalCovidEntriesByCountryByDays()
//    {
//        System.out.println("getInternalCovidEntryForCountryByCodeByDate");
//        CountryFacade instance = null;
//        instance.getInternalCovidEntryForCountryByCodeByDate();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of persistAllExternalCountries method, of class CountryFacade.
//     */
//    @Test
//    public void testPersistAllExternalCountries() throws Exception
//    {
//        System.out.println("persistAllExternalCountries");
//        List<CountryExDTO> DTOList = null;
//        CountryFacade instance = null;
//        instance.persistAllExternalCountries(DTOList);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of persistAllExternalCovidEntriesForCountryByCode method, of class CountryFacade.
//     */
//    @Test
//    public void testPersistAllExternalCovidEntriesForCountryByCode() throws Exception
//    {
//        System.out.println("persistAllExternalCovidEntriesForCountryByCode");
//        List<CovidExDTO> exDTOList = null;
//        String code = "";
//        CountryFacade instance = null;
//        instance.persistAllExternalCovidEntriesForCountryByCode(exDTOList, code);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
