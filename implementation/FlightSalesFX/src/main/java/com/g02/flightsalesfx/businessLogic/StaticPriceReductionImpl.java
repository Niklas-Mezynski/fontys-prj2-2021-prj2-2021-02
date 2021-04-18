package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;

import java.time.LocalDate;

@TableName("staticpriceredutions")
public class StaticPriceReductionImpl extends PriceReductionImpl implements Savable {

    @PrimaryKey
    public String name;
    public LocalDate endDate;

    // as 0.10 / 0.09 ...
    public double percentageAsDouble;

    public StaticPriceReductionImpl(String name, LocalDate end, double percentage) {
        super(name, end);
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
    public String getName() {
        return name;
    }

    @Override
    public double getPercentageAsDouble() {
        return percentageAsDouble;
    }
}
