/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import rest.CountryResource;
import facades.CountryFacade;
import java.io.IOException;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Christian
 */
public class SetUpCountriessfromExAPI
{
    private static EntityManagerFactory emf;
    private static CountryFacade facade;
    private static CountryResource rest;
    public static void main(String[] args) throws IOException
    {
        rest.fillDbWithCountry(); 
    }
}
