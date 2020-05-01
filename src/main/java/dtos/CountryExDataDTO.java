/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

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

    public CountryExDataDTO(String name, String alpha2Code, long population)
    {
        this.name = name;
        this.alpha2Code = alpha2Code;
        this.population = population;
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
