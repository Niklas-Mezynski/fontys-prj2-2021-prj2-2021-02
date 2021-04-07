/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.time.LocalDateTime;

/**
 *
 * @author anato
 */
public interface FlightManager {

    public Flight createFlight(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price);
}
