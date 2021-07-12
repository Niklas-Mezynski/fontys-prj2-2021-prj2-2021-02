package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@TableName("staticpriceredutions")
public class StaticPriceReductionImpl implements Savable,PriceReduction {

    @PrimaryKey
    public String name;

    public LocalDateTime endDate;
    public LocalDateTime startDate;
    public boolean isPercent;
    // as 0.10 / 0.09 ...
    public double reductionPercentage;

    private StaticPriceReductionImpl(){
    }

    public StaticPriceReductionImpl(String name, LocalDateTime end, LocalDateTime start,boolean isPercentage, double percentage) {
        this.name = name;
        this.endDate = end;
        this.startDate=start;
        this.isPercent=isPercentage;
        this.reductionPercentage=percentage;
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
        return reductionPercentage;
    }

    public static StaticPriceReductionImpl of(PriceReduction priceReduction) {
        return new StaticPriceReductionImpl(priceReduction.getName(), priceReduction.getEndTime(), priceReduction.getStartTime(), priceReduction.isPercentage(), priceReduction.getPercentageAsDouble());
    }

    @Override
    public String toString() {
        return "StaticPriceRed.[" +
                "name='" + name + '\'' +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", isPercent=" + isPercent +
                ", reductionPercentage=" + reductionPercentage +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaticPriceReductionImpl that = (StaticPriceReductionImpl) o;
        return isPercent == that.isPercent && Double.compare(that.reductionPercentage, reductionPercentage) == 0 && name.equals(that.name) && endDate.equals(that.endDate) && startDate.equals(that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, endDate, startDate, isPercent, reductionPercentage);
    }
}
