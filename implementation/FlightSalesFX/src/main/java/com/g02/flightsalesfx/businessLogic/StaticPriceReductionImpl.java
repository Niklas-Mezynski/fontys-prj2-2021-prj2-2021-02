package com.g02.flightsalesfx.businessLogic;

import java.time.LocalDate;

public class StaticPriceReductionImpl extends PriceReductionImpl {

    private String name;
    private LocalDate endDate;

    // as 0.10 / 0.09 ...
    private double percentageAsDouble;

    public StaticPriceReductionImpl(String name, LocalDate end, double percentage) {
        this.name = name;
        this.endDate = end;
        this.percentageAsDouble = percentage;
    }

    /**
     * @return The LocalDate that this PriceReduction expires on
     */
    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    String getName() {
        return name;
    }

    @Override
    public double getPercentageAsADouble() {
        return percentageAsDouble;
    }
}
