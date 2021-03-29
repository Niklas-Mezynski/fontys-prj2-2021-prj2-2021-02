/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.util.List;

public interface Plane {

    String getName();

    String getManufacturer();

    String getType();

    void addSeat(Seat s);

    void addAllSeats(List<? extends Seat> seatList);
}
