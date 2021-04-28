package org.g02.flightsalesfx.persistence;

import org.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.List;

/**
 *
 * @author anato
 */
public interface SeatOptionsStorageService {
    SeatOption add(SeatOption s);
    
    List<SeatOption> getAll();
}
