package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.PriceReduction;

import java.time.LocalDate;

public abstract class PriceReductionImpl implements PriceReduction {
    private String name;
    private LocalDate endDate;
    private double reductionPercentage;

    public PriceReductionImpl (String name, LocalDate endDate) {
        this.name = name;
        this.endDate = endDate;
    }
}
