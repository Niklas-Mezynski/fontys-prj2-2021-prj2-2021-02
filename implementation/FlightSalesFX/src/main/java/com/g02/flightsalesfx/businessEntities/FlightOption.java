/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

/**
 *
 * @author anato
 */
public interface FlightOption extends Option{

    /**
     * @return How often this FlightOption is currently available
     */
    public int getCurrentAvailability();

    /**
     * @return How often this FlightOption was available at the start of the Sales Process
     */
    public int getMaxAvailability();

    /**
     * @return The Flight that this FlightOption belongs to
     */
    public Flight getFlight();
    
}
