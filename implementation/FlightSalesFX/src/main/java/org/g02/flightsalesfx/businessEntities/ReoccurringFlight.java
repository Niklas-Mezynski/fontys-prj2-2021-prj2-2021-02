/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import java.time.Duration;

/**
 *
 * @author anato
 */
public interface ReoccurringFlight extends Flight{

    /**
     * @return the Interval at which this Flight should be renewed
     */
    public Duration getInterval();

    public Flight getFlight();

}
