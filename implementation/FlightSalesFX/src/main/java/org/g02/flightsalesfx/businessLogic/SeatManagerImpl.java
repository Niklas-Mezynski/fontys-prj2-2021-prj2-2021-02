package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessEntities.SeatManager;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.persistence.SeatStorageService;

import java.util.List;

public class SeatManagerImpl implements SeatManager {

    private SeatStorageService seatStorageService;

    @Override
    public Seat createSeat(int row, int seat, List<SeatOption> seatOptions) {
        return new SeatImpl(row, seat, seatOptions);
    }

    public void setSeatStorageService(SeatStorageService seatStorageService) {
        this.seatStorageService = seatStorageService;
    }
}
