/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author anato
 */
public interface Flight {
    /**
     * Starts the Sales process for this Flight
     */
    public void startSalesProcess();

    /**
     * @return Flight number of this Flight
     */
    public int getFlightNumber();

    /**
     * @return Current Price of this Flight
     */
    public double getPrice();

    /**
     * @return Route of this Flight
     */
    public Route getRoute();

    /**
     * @return Whether the Sales Process has started for this Flight
     */
    public boolean getSalesProcessStatus();

    /**
     * @return The Date that this Plane should/has arrived
     */
    public LocalDateTime getArrival();

    /**
     * @return The Date that this Plane is due to/has departed
     */
    public LocalDateTime getDeparture();

    /**
     * @return The SalesOfficer that scheduled this Flight
     */
    public SalesOfficer getCreatedBy();

    /**
     * @return The List of PriceReductions that apply for this Flight
     */
    public List<PriceReduction> getPriceReductions();

    /**
     * @return The Plane that this Flight uses
     */
    public Plane getPlane();

    /**
     * @return The List of FlightOptions that are available on this Flight
     */
    public List<FlightOption> getFlightOptions();
    
    public PriceReduction applyPriceReduction(PriceReduction p);

    public void setArrival(LocalDateTime newArrival);

    public void setDeparture(LocalDateTime newDeparture);
}
