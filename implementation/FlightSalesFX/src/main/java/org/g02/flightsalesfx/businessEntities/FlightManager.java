/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import java.time.LocalDateTime;

/**
 *
 * @author anato
 */
public interface FlightManager {

    public Flight createFlight(SalesOfficer creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price);
}
