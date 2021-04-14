/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import com.g02.flightsalesfx.persistence.FlightStorageService;

/**
 *
 * @author anato
 */
public interface ReoccurringFlightManager {

    ReoccurringFlight createRoccurringFlight(Flight flight, int interval);


}
