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
public interface ReoccurringFlight extends Flight{

    /**
     * @return the Interval at which this Flight should be renewed
     */
    public int getInterval();
    
}
