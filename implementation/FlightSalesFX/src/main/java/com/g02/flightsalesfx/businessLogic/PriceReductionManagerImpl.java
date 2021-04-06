package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.PriceReductionManager;
import com.g02.flightsalesfx.persistence.PriceReductionStorageService;

import java.time.LocalDate;

public class PriceReductionManagerImpl implements PriceReductionManager {

    private PriceReductionStorageService priceReductionStorageService;

    @Override
    public StaticPriceReductionImpl createStaticPriceReduction(String name, LocalDate endDate, double percentage) {
        return new StaticPriceReductionImpl(name, endDate, percentage);
    }

    @Override
    public DynamicPriceReductionImpl createDynamicPriceReduction(String name, LocalDate endDate) {
        return new DynamicPriceReductionImpl(name, endDate);
    }

    public void setPriceReductionStorageService(PriceReductionStorageService priceReductionStorageService) {
        this.priceReductionStorageService = priceReductionStorageService;
    }
}
