/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Christian
 */
public class CountryExDataDTO
{
    private String countryName, countryCode;
    private long population;

    public CountryExDataDTO(String countryName, String countryCode, long population)
    {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.population = population;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
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
        return "CountryExDataDTO{" + "countryName=" + countryName + ", countryCode=" + countryCode + ", population=" + population + '}';
    }
    
}
