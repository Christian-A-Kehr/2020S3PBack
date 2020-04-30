/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.CovidData;

/**
 *
 * @author Brandstrup
 */
public class CovidExDTO
{

    String date;
    long newConfirmedInfected, totalConfirmedInfected, newRecovered, totalRecovered, newDeaths, totalDeaths;
    CountryBasicInDTO country;

    public CovidExDTO(CovidData cov)
    {
        this.date = String.valueOf(cov.getDate());
        this.newConfirmedInfected = cov.getNewConfirmedInfected();
        this.totalConfirmedInfected = cov.getTotalConfirmedInfected();
        this.newRecovered = cov.getNewRecovered();
        this.totalRecovered = cov.getTotalRecovered();
        this.newDeaths = cov.getNewDeaths();
        this.totalDeaths = cov.getTotalDeaths();
        this.country = new CountryBasicInDTO(cov.getCountry());
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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

    public CountryBasicInDTO getCountry()
    {
        return country;
    }

    public void setCountry(CountryBasicInDTO country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "CovidExDTO{" + "date=" + date + ", newConfirmedInfected="
                + newConfirmedInfected + ", totalConfirmedInfected="
                + totalConfirmedInfected + ", newRecovered=" + newRecovered
                + ", totalRecovered=" + totalRecovered + ", newDeaths="
                + newDeaths + ", totalDeaths=" + totalDeaths + ", country="
                + country + '}';
    }

}
