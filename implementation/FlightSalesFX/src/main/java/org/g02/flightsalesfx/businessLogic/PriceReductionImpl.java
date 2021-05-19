package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class PriceReductionImpl implements PriceReduction {
    private String name;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private boolean isPercent;
    private double reductionPercentage;

    public PriceReductionImpl(){}


    public PriceReductionImpl (String name,LocalDateTime endTime, LocalDateTime startTime, boolean isPercent,double reductionPercentage) {
        this.name = name;
        this.endTime = endTime;
        this.startTime=startTime;
        this.isPercent=isPercent;
        this.reductionPercentage=reductionPercentage;
    }

    @Override
    public String toString() {
        return "PriceReductionImpl{" +
                "name='" + name + '\'' +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", isPercent=" + isPercent +
                ", reductionPercentage=" + reductionPercentage +
                '}';
    }
}
