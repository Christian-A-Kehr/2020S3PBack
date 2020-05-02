/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.CountryData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class CountriesDTO
{
    List<CountryExDataDTO> all = new ArrayList();

    public CountriesDTO(List<CountryData> countryEntities )
    {
        countryEntities.forEach((p) ->{ all.add(new CountryExDataDTO(p));}
        );
    }
    
    
    
}
