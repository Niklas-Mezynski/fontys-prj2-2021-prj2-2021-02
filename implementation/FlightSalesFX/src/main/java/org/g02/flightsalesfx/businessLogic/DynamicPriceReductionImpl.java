package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("dynamicpriceredutions")
public class DynamicPriceReductionImpl extends PriceReductionImpl implements Savable {

    @PrimaryKey
    public String name;
    public String source;
    public LocalDateTime endDate;

    public LocalDateTime startDate;
    public boolean isPercentage;

    // external source that determines the characteristics of this dynamic price-reduction
    //private var source;

    // todo: implement source
    public DynamicPriceReductionImpl(String name, String source, LocalDateTime end, LocalDateTime start,boolean isPercentage, double reductionPercentage) {
        super(name, end,start,isPercentage,reductionPercentage);
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
        return isPercentage;
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
