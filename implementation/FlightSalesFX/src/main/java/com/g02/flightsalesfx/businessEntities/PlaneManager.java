/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.businessEntities;

/**
 *
 * @author anato
 */
public interface PlaneManager {

    /**
     * @param name Name of the Plane to create
     * @param manufacturer Manufacturer of the Plane to create
     * @param type Type of the Plane to create
     * @return The newly created Plane
     */
    public Plane createPlane(String name, String manufacturer, String type);

}
