/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Christian and Brandstrup
 */
@Entity
@NamedQuery(name = "CountryData.deleteAllRows", query = "DELETE from CountryData")
@Table(name = "countries")
public class CountryData implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String countryName;
    @Column(nullable = false, unique = true)
    private String countryCode;
    private long population;

    @ManyToMany(mappedBy = "countryTracked")
    private Set<User> userTrackers;

    @OneToMany(mappedBy = "country", cascade =
    {
        CascadeType.MERGE, CascadeType.PERSIST
    })
    private Set<CovidData> covidEntries;

    public CountryData()
    {
    }

    public CountryData(
            String countryName, String countryCode, long population,
            Set<User> userTrackers, Set<CovidData> covidEntries)
    {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.population = population;
        this.userTrackers = userTrackers;
        this.covidEntries = covidEntries;
    }
    
    /**
     * Used to get external countries with no id.
     * 
     * @param countryName
     * @param countryCode
     * @param population 
     */
    public CountryData(String countryName, String countryCode, long population)
    {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.population = population;
    }

    public long getId()
    {
        return id;
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

    public Set<User> getUserTrackers()
    {
        return userTrackers;
    }

    public void addUserTrackers(User user)
    {
        this.userTrackers.add(user);
        user.getCountryTracked().add(this);
    }

    public void removeUserTrackers(User user)
    {
        this.userTrackers.remove(user);
        user.getCountryTracked().remove(this);
    }

    public Set<CovidData> getCovidEntries()
    {
        return covidEntries;
    }

    public void addCovidEntry(CovidData entry)
    {
        this.covidEntries.add(entry);
        entry.setCountry(this);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.countryCode);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CountryData other = (CountryData) obj;
        if (!Objects.equals(this.countryCode, other.countryCode))
        {
            return false;
        }
        return true;
    }

}
