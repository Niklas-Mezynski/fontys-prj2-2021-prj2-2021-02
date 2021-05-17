package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.PriceReductionManager;
import org.g02.flightsalesfx.persistence.PriceReductionStorageService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PriceReductionManagerImpl implements PriceReductionManager {

    private PriceReductionStorageService priceReductionStorageService;

    @Override
    public StaticPriceReductionImpl createStaticPriceReduction(String name, LocalDateTime endDate, LocalDateTime start,boolean isPercentage, double percentage) {
        return new StaticPriceReductionImpl(name, endDate,start,isPercentage, percentage);
    }

    @Override
    public DynamicPriceReductionImpl createDynamicPriceReduction(String name, String source, LocalDateTime endDate,LocalDateTime start,boolean ispercentage,double reduction) {
        return new DynamicPriceReductionImpl(name, source, endDate,start,ispercentage,reduction);
    }

    public void setPriceReductionStorageService(PriceReductionStorageService priceReductionStorageService) {
        this.priceReductionStorageService = priceReductionStorageService;
    }
}
