/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import org.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl;
import org.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author anato
 */
public interface PriceReductionManager {

    public StaticPriceReductionImpl createStaticPriceReduction(String name, LocalDateTime endDate,LocalDateTime start,boolean isPercentage, double percentage);

    // todo: source
    public DynamicPriceReductionImpl createDynamicPriceReduction(String name, String source, LocalDateTime endDate,LocalDateTime start,boolean isPercentage, double percentage);

}
