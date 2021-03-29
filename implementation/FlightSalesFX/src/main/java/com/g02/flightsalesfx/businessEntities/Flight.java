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
    public void startSalesProcess();
    
    public void applyPriceReduction(PriceReduction p);
    
    public int getFlightNumber();
    
    public double getPrice();
    
    public Route getRoute();
    
    public boolean getSalesProcessStatus();
    
    public LocalDateTime getArrival();
    
    public LocalDateTime getDeparture();
    
    public SalesOfficer getCreatedBy();
    
    public List<PriceReduction> getPriceReductions();
    
    public Plane getPlane();
    
    public List<FlightOption> getFlightOptions();
    
    
}
