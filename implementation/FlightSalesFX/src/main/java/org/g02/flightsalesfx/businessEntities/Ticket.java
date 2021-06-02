/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author anato
 */
public interface Ticket {
    /**
     * @return The Flight that this Ticket belongs to
     */

    public Flight getFlight();

    public Seat getSeat();

    public List<SeatOption> getBookedSeatOption();

    public String getFirstName();

    public String getLastName();

    public SeatOption[] getSeatOptions();

    int getID();
}
