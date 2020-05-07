/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Brandstrup
 */
public class CovidExDTO
{

    long Confirmed, Deaths, Recovered;
    String Date, Province;

    public CovidExDTO(long Confirmed, long Deaths, long Recovered, String Date, String Province)
    {
        this.Confirmed = Confirmed;
        this.Deaths = Deaths;
        this.Recovered = Recovered;
        this.Date = Date;
        this.Province = Province;
    }

    public long getConfirmed()
    {
        return Confirmed;
    }

    public void setConfirmed(long Confirmed)
    {
        this.Confirmed = Confirmed;
    }

    public long getDeaths()
    {
        return Deaths;
    }

    public void setDeaths(long Deaths)
    {
        this.Deaths = Deaths;
    }

    public long getRecovered()
    {
        return Recovered;
    }

    public void setRecovered(long Recovered)
    {
        this.Recovered = Recovered;
    }

    public String getDate()
    {
        return Date;
    }

    public void setDate(String Date)
    {
        this.Date = Date;
    }

    public String getProvince()
    {
        return Province;
    }

    public void setProvince(String Province)
    {
        this.Province = Province;
    }

}
