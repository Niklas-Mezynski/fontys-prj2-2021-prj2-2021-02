package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.PriceReduction;
import com.g02.flightsalesfx.businessEntities.PriceReductionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceReductionStorageServiceImpl implements PriceReductionStorageService{

    private List<PriceReduction> reductions;

    public PriceReductionStorageServiceImpl (PriceReductionManager priceReductionManager) {
        this.reductions = new ArrayList<>();
    }

    @Override
    public boolean add(PriceReduction priceReduction) {
        reductions.add(priceReduction);
        return true;
    }

    @Override
    public List<PriceReduction> getAll() {
        return Collections.unmodifiableList(reductions);
    }
}
