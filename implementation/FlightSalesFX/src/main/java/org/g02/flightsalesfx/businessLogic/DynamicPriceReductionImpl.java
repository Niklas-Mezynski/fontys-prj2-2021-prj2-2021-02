package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("dynamicpriceredutions")
public class DynamicPriceReductionImpl implements Savable, PriceReduction {

    @PrimaryKey
    public String name;
    public String source;
    public LocalDateTime endDate;
    public LocalDateTime startDate;
    public boolean isPercent;
    // as 0.10 / 0.09 ...
    public double reductionPercentage;

    // external source that determines the characteristics of this dynamic price-reduction
    //private var source;

    private DynamicPriceReductionImpl(){

    }

    // todo: implement source
    public DynamicPriceReductionImpl(String name, String source, LocalDateTime end, LocalDateTime start,boolean isPercentage, double reductionPercentage) {
        this.name=name;
        this.endDate=end;
        this.startDate=start;
        this.isPercent=isPercentage;
        this.reductionPercentage=reductionPercentage;
        this.source = source;
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
        return isPercent;
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

    @Override
    public String toString() {
        return "DynamicPriceRed.[" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", isPercent=" + isPercent +
                ", reductionPercentage=" + reductionPercentage +
                ']';
    }
}
