/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

import java.util.List;

/**
 * @author anato
 */
public interface PlaneManager {

    /**
     * @param name         Name of the Plane to create
     * @param manufacturer Manufacturer of the Plane to create
     * @param type         Type of the Plane to create
     * @return The newly created Plane
     */
    public Plane create(String name, String manufacturer, String type);

    public Plane create(String name, String manufacturer, String type, List<Seat> seats);

    List<Plane> getAll();

    Plane add(Plane plane);

    Plane update(Plane plane);
}
