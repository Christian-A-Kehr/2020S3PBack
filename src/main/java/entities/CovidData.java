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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Brandstrup
 */
@Entity
@NamedQuery(name = "CovidData.deleteAllRows", query = "DELETE from CovidData")
@Table(name = "covidEntries",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"DATE", "COUNTRY_CODE"})}
)
public class CovidData implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRYCODE")
    private CountryData country;

    long newConfirmedInfected, totalConfirmedInfected, newRecovered, totalRecovered, newDeaths, totalDeaths;

    public CovidData()
    {
    }

    public CovidData(
            Date date, CountryData country, long newConfirmedInfected,
            long totalConfirmedInfected, long newRecovered, long totalRecovered,
            long newDeaths, long totalDeaths)
    {
        this.date = date;
        this.country = country;
        this.newConfirmedInfected = newConfirmedInfected;
        this.totalConfirmedInfected = totalConfirmedInfected;
        this.newRecovered = newRecovered;
        this.totalRecovered = totalRecovered;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
    }

    public long getId()
    {
        return id;
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert)
    {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.of("UTC"))
                .toInstant());
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert)
    {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    /**
     * Retrieves the Date as a LocalDate.
     *
     * @return a LocalDateTime.
     */
    public LocalDateTime getLocalDate()
    {
        return convertToLocalDateTimeViaInstant(date);
    }

    /**
     * Retrieves the Date as a java.util.Date.
     *
     * @return a Date.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Updates the Date using a LocalDate format.
     *
     * @param date LocalDate, use LocalDate.now() for the current time and use
     * LocalDate.of(2001, Month.MARCH, 13) [as an example] for specific dates.
     */
    public void setDate(LocalDate date)
    {
        this.date = convertToDateViaInstant(date);
    }

    /**
     * Updates the Date using a java.util.Date format.
     *
     * @param date A regular java.util.Date, which is the SQL's preferred
     * format.
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    public CountryData getCountry()
    {
        return country;
    }

    /**
     * Don't use this method. Instead call addCovidEntry from the CountryData
     * you are adding this entry to.
     */
    public void setCountry(CountryData country)
    {
        this.country = country;
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
    public int hashCode()
    {
        return Objects.hash(this.date);
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
        final CovidData other = (CovidData) obj;
        if (!Objects.equals(this.date.getTime(), other.date.getTime()))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "CovidData{"
                + "id=" + id
                + ", date=" + this.getLocalDate().toString()
                + ", country=" + country.getCountryName()
                + ", newConfirmedInfected=" + newConfirmedInfected
                + ", totalConfirmedInfected=" + totalConfirmedInfected
                + ", newRecovered=" + newRecovered
                + ", totalRecovered=" + totalRecovered
                + ", newDeaths=" + newDeaths
                + ", totalDeaths=" + totalDeaths
                + '}';
    }

}
