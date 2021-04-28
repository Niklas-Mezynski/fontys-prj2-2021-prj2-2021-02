/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;

/**
 *
 * @author anato
 */
public interface Route {

    /**
     * @return The Airport that the Plane should depart from
     */
    public Airport getDepartureAirport();

    /**
     * @return The Airport that the Plane should arrive at
     */
    public Airport getArrivalAirport();


    /**
     *
     * @param newAp Airport to be set as Arrival Airport
     */
    public void setArrivalAirport(Airport newAp);

    /**
     *
     * @param newAp Airport to be set as Departure Airport
     */
    public void setDepartureAirport(Airport newAp);

    /**
     *
     * @return true if a creation of a new flight with this route is allowed
     */
    public boolean getEnabled();

    /**
     * Enable the route to be used in new flights
     */
    public void enableRoute();

    /**
     * Disable the route to be used in new flights
     */
    public void disableRoute();

    /**
     * toggles the enable-state of the route
     */
    public void toggleEnable();

    int getId();
}
