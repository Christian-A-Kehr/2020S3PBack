/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.CountryData;
import java.util.Set;

/**
 *
 * @author Christian
 */
public class CountryExDataDTO
{
    //alpha2Code = country code
    private String name, alpha2Code;
    private long population;

   

    public CountryExDataDTO(CountryData cd)
    {
        this.name = cd.getCountryName();
        this.alpha2Code = cd.getCountryCode();
        this.population = cd.getPopulation();
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAlpha2Code()
    {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code)
    {
        this.alpha2Code = alpha2Code;
    }

    public long getPopulation()
    {
        return population;
    }

    public void setPopulation(long population)
    {
        this.population = population;
    }

    
    
    @Override
    public String toString()
    {
        return "CountryExDataDTO{" + "countryName=" + name + ", countryCode=" + alpha2Code + ", population=" + population + '}';
    }
    
}
