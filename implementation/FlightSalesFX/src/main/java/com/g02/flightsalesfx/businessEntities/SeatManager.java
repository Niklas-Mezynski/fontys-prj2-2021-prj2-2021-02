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
    public Seat createSeat(int row, int seat, List<SeatOption> seatOptions);
}
