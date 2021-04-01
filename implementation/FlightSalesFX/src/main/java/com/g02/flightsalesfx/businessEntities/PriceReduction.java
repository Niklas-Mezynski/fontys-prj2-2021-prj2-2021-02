/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.time.LocalDate;

/**
 *
 * @author anato
 */
public interface PriceReduction {

    /**
     * @return The LocalDate that this PriceReduction expires on
     */
    public LocalDate getEndDate();
    
}
