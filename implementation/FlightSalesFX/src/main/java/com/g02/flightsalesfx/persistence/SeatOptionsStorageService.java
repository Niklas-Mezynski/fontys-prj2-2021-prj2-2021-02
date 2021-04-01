package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.SeatOption;
import java.util.List;

/**
 *
 * @author anato
 */
public interface SeatOptionsStorageService {
    void add(SeatOption s);
    
    List<SeatOption> getAll();
}
