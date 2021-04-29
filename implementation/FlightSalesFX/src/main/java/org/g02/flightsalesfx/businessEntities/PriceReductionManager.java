/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import org.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl;
import org.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;

import java.time.LocalDate;

/**
 *
 * @author anato
 */
public interface PriceReductionManager {

    public StaticPriceReductionImpl createStaticPriceReduction(String name, LocalDate endDate, double percentage);

    // todo: source
    public DynamicPriceReductionImpl createDynamicPriceReduction(String name, String source, LocalDate endDate);

}