/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.util.List;

/**
 *
 * @author
 */
public interface SeatManager {
    /**
     * @param row Row Number of the new Seat
     * @param seat Seat Number of the new Seat in the Row
     * @param seatOptions SeatOptions that the Seat should have
     * @return The created Seat
     */
    public Seat createSeat(int row, int seat, List<SeatOption> seatOptions);
}
