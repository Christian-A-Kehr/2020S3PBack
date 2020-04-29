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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Christian and Brandstrup
 */
@Entity
@Table(name = "countries")
@NamedQuery(name = "countries.deleteAllRows", query = "DELETE from countries")
public class Country implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String countryName;
    @Column(nullable = false, unique = true)
    private String countryCode;
    private long population, newConfirmedInfected, totalConfirmedInfected, newRecovered, totalRecovered, newDeaths, totalDeaths;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @ManyToMany(mappedBy = "countryTracked")
    private Set<User> userTrackers;

    public Country()
    {
    }

    public Country(
            String countryName, String countryCode, long population,
            long newConfirmedInfected, long totalConfirmedInfected,
            long newRecovered, long totalRecovered, long newDeaths,
            long totalDeaths, Date date)
    {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.population = population;
        this.newConfirmedInfected = newConfirmedInfected;
        this.totalConfirmedInfected = totalConfirmedInfected;
        this.newRecovered = newRecovered;
        this.totalRecovered = totalRecovered;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
        this.date = date;
        this.userTrackers = userTrackers;
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert)
    {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant().plusSeconds(86400));
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert)
    {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Long getId()
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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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
        final Country other = (Country) obj;
        if (!Objects.equals(this.countryCode, other.countryCode))
        {
            return false;
        }
        return true;
    }

}
