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

    long confirmed, deaths, recovered;
    String date;

    public CovidExDTO(long confirmed, long deaths, long recovered, String date)
    {
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.date = date;
    }

    public long getConfirmed()
    {
        return confirmed;
    }

    public void setConfirmed(long confirmed)
    {
        this.confirmed = confirmed;
    }

    public long getDeaths()
    {
        return deaths;
    }

    public void setDeaths(long deaths)
    {
        this.deaths = deaths;
    }

    public long getRecovered()
    {
        return recovered;
    }

    public void setRecovered(long recovered)
    {
        this.recovered = recovered;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

}
