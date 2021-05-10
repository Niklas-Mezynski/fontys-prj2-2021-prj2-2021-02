package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.util.List;

public interface PriceReductionStorageService {

    PriceReduction add(PriceReduction priceReduction);

    List<PriceReduction> getAll();
}
