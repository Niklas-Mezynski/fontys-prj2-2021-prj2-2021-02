/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

/**
 *
 * @author anato
 */
public interface OptionManager {

    /**
     * @param name The Name of the SeatOption that should be created
     * @return The newly created SeatOption
     */
    SeatOption createSeatOption(String name, double price);

    FlightOption createFlightOption(String name, int maxAvailable, double price);
}
