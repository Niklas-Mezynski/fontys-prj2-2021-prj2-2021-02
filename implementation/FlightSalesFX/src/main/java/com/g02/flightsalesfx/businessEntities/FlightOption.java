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
    public int getMaxAvailability();
    
    public Flight getFlight();
    
}
