/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

/**
 *
 * @author anatol
 */
public interface Airport {
    /**
     *
     * @return Name of the airport
     */
    public String getName();

    /**
     *
     * @return Name of the city that the airport is in
     */
    public String getCity();

    /**
     *
     * @return Name of the country that the airport is in
     */
    public String getCountry();
    
}
