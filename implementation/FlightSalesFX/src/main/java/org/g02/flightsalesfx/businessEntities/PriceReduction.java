/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author anato
 */
public interface PriceReduction {

    /**
     * @return The LocalDate that this PriceReduction expires on
     */
    public LocalDateTime getEndTime();

    public LocalDateTime getStartTime();

    public boolean isPercentage();

    public String getName();

    public abstract double getPercentageAsDouble();
}
