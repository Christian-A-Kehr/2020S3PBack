/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Country;

/**
 *
 * @author Brandstrup
 */
public class CountryInDTO
{

    String countryName, countryCode, date;
    long population, newConfirmedInfected, totalConfirmedInfected, newRecovered,
            totalRecovered, newDeaths, totalDeaths;

    public CountryInDTO(Country c)
    {
        this.countryName = c.getCountryName();
        this.countryCode = c.getCountryCode();
        this.date = String.valueOf(c.getDate());
        this.population = c.getPopulation();
        this.newConfirmedInfected = c.getNewConfirmedInfected();
        this.totalConfirmedInfected = c.getTotalConfirmedInfected();
        this.newRecovered = c.getNewRecovered();
        this.totalRecovered = c.getTotalRecovered();
        this.newDeaths = c.getNewDeaths();
        this.totalDeaths = c.getTotalDeaths();
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public long getPopulation()
    {
        return population;
    }

    public void setPopulation(long population)
    {
        this.population = population;
    }

    public long getNewConfirmedInfected()
    {
        return newConfirmedInfected;
    }

    public void setNewConfirmedInfected(long newConfirmedInfected)
    {
        this.newConfirmedInfected = newConfirmedInfected;
    }

    public long getTotalConfirmedInfected()
    {
        return totalConfirmedInfected;
    }

    public void setTotalConfirmedInfected(long totalConfirmedInfected)
    {
        this.totalConfirmedInfected = totalConfirmedInfected;
    }

    public long getNewRecovered()
    {
        return newRecovered;
    }

    public void setNewRecovered(long newRecovered)
    {
        this.newRecovered = newRecovered;
    }

    public long getTotalRecovered()
    {
        return totalRecovered;
    }

    public void setTotalRecovered(long totalRecovered)
    {
        this.totalRecovered = totalRecovered;
    }

    public long getNewDeaths()
    {
        return newDeaths;
    }

    public void setNewDeaths(long newDeaths)
    {
        this.newDeaths = newDeaths;
    }

    public long getTotalDeaths()
    {
        return totalDeaths;
    }

    public void setTotalDeaths(long totalDeaths)
    {
        this.totalDeaths = totalDeaths;
    }

    @Override
    public String toString()
    {
        return "CountryInDTO{" + "countryName=" + countryName + ", countryCode="
                + countryCode + ", date=" + date + ", population=" + population
                + ", newConfirmedInfected=" + newConfirmedInfected
                + ", totalConfirmedInfected=" + totalConfirmedInfected
                + ", newRecovered=" + newRecovered + ", totalRecovered="
                + totalRecovered + ", newDeaths=" + newDeaths
                + ", totalDeaths=" + totalDeaths + '}';
    }

}
