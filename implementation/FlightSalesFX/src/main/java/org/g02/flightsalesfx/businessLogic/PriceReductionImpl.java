package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class PriceReductionImpl implements PriceReduction {
    protected String name;
    protected LocalDateTime endDate;
    protected LocalDateTime startDate;
    protected boolean isPercent;
    protected double reductionPercentage;

    public PriceReductionImpl(){}


    public PriceReductionImpl (String name,LocalDateTime endTime, LocalDateTime startTime, boolean isPercent,double reductionPercentage) {
        this.name = name;
        this.endDate = endTime;
        this.startDate=startTime;
        this.isPercent=isPercent;
        this.reductionPercentage=reductionPercentage;
    }

    @Override
    public String toString() {
        return "PriceReductionImpl{" +
                "name='" + name + '\'' +
                ", endTime=" + endDate +
                ", startTime=" + startDate +
                ", isPercent=" + isPercent +
                ", reductionPercentage=" + reductionPercentage +
                '}';
    }
}
