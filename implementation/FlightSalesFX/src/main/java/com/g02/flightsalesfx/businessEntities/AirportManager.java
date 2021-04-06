/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import com.g02.flightsalesfx.persistence.AirportStorageService;

/**
 *
 * @author anatol
 */
public interface AirportManager {
    public Airport createAirport(String name, String city, String country);
}
