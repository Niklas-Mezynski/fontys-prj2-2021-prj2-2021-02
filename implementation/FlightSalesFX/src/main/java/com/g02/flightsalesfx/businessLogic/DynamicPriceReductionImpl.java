package com.g02.flightsalesfx.businessLogic;

import java.time.LocalDate;

public class DynamicPriceReductionImpl extends PriceReductionImpl {

    private String name;
    private LocalDate endDate;
    // external source that determines the characteristics of this dynamic price-reduction
    //private var source;

    // todo: implement source
    public DynamicPriceReductionImpl(String name, LocalDate end) {
        this.name = name;
        this.endDate = end;

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
}
