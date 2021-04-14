package com.g02.flightsalesfx.businessLogic;

import java.time.LocalDate;

public class DynamicPriceReductionImpl extends PriceReductionImpl {

    private String name;
    private String source;
    private LocalDate endDate;

    // external source that determines the characteristics of this dynamic price-reduction
    //private var source;

    // todo: implement source
    public DynamicPriceReductionImpl(String name, String source, LocalDate end) {
        super(name, end);
        this.source = source;
    }
    /**
     * @return The LocalDate that this PriceReduction expires on
     */
    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPercentageAsDouble() {
        //TODO Calculate the PriceReduction depending on the @Source
        return 0;
    }
}
