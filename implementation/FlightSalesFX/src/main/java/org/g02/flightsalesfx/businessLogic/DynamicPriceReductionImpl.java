package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;

import java.time.LocalDate;

@TableName("dynamicpriceredutions")
public class DynamicPriceReductionImpl extends PriceReductionImpl implements Savable {

    @PrimaryKey
    public String name;
    public String source;
    public LocalDate endDate;

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
