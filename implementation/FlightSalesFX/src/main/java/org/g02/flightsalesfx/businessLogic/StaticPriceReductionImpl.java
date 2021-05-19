package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("staticpriceredutions")
public class StaticPriceReductionImpl extends PriceReductionImpl implements Savable {

    @PrimaryKey
    public String name;
    public LocalDateTime endDate;
    public LocalDateTime startDate;
    public boolean isPercentage;

    // as 0.10 / 0.09 ...
    public double percentageAsDouble;

    private StaticPriceReductionImpl(){
    }

    public StaticPriceReductionImpl(String name, LocalDateTime end, LocalDateTime start,boolean isPercentage, double percentage) {
        super(name, end,start,isPercentage,percentage);
        this.percentageAsDouble = percentage;
        this.name=name;
    }

    /**
     * @return The LocalDate that this PriceReduction expires on
     */
    @Override
    public LocalDateTime getEndTime() {
        return endDate;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startDate;
    }

    @Override
    public boolean isPercentage() {
        return isPercentage;
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
