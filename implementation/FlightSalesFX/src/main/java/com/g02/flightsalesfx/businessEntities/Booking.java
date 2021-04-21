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
public interface Booking {
    /**
     *
     * @return returns the salesEmployee that created this booking
     */
    public SalesEmployee getSalesEmployee();

    public List<Ticket> getTickets();

    public List<FlightOption> getBookedFlightOptions();

    public String getCustomerEmail();

    public Flight getFlight();

    public void addTicket(Ticket t);

    void removeTicket(Ticket ticket);
}
