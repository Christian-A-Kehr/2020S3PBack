/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.CountryData;

/**
 *
 * @author Brandstrup
 */
public class CountryBasicInDTO
{
    String countryName, countryCode;
    
    public CountryBasicInDTO(CountryData c)
    {
        this.countryName = c.getCountryName();
        this.countryCode = c.getCountryCode();
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

    @Override
    public String toString()
    {
        return "CountryBasicInDTO{" + "countryName=" + countryName 
                + ", countryCode=" + countryCode + '}';
    }
    
    
}
