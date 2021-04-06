/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import com.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl;
import com.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;

import java.time.LocalDate;

/**
 *
 * @author anato
 */
public interface PriceReductionManager {

    public StaticPriceReductionImpl createStaticPriceReduction(String name, LocalDate endDate, double percentage);

    public DynamicPriceReductionImpl createDynamicPriceReduction(String name, LocalDate endDate);

}
