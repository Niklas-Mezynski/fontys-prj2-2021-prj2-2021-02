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
public interface Plane {
    
    public String getName();
    
    public String getManufacturer();
    
    public String getType();
    
    public List<Seat> getSeats();
    
}
