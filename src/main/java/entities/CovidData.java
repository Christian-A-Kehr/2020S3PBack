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
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Brandstrup
 */
@Entity
@NamedQuery(name = "CovidData.deleteAllRows", query = "DELETE from CovidData")
@Table(name = "covidEntries")
public class CovidData implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @ManyToOne
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

//    private Date convertToDateViaInstant(LocalDate dateToConvert)
//    {
//        return java.util.Date.from(dateToConvert.atStartOfDay()
//                .atZone(ZoneId.systemDefault())
//                .toInstant().plusSeconds(86400));
//    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert)
    {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
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
     * Retrieves the Date as a java.sql.Date.
     *
     * @return a Date.
     */
    public Date getDate()
    {
        return date;
    }

//    /**
//     * Updates the Date using a LocalDate format.
//     *
//     * @param date LocalDate, use LocalDate.now() for the current time and use
//     * LocalDate.of(2001, Month.MARCH, 13) [as an example] for specific dates.
//     */
//    public void setDate(LocalDate date)
//    {
//        this.date = convertToDateViaInstant(date);
//    }

    /**
     * Updates the Date using a java.sql.Date format.
     *
     * @param date A regular java.sql.Date, which is the SQL's preferred
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
        if (!Objects.equals(this.date, other.date))
        {
            return false;
        }
        return true;
    }

}
