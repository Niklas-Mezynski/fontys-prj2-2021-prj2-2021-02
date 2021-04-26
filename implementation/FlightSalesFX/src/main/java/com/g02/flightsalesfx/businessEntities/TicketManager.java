/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.util.List;

/**
 *
 * @author anato
 */
public interface TicketManager {

    public Ticket createTicket(Flight flight, Seat seat,  String passengerFName, String passengerLName, SeatOption[] bookedSeatOptions);
    
}
