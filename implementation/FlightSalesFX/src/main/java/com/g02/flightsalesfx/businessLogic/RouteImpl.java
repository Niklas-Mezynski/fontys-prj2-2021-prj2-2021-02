package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;

public class RouteImpl implements Route {
    private  Airport departureAirport;
    private  Airport arrivalAirport;
    private boolean rteEnabled;

    public RouteImpl (Airport departureAirport, Airport arrivalAirport) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.rteEnabled = true;
    }

    public RouteImpl (Airport departureAirport, Airport arrivalAirport, boolean rteEnabled) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.rteEnabled = rteEnabled;
    }

    /**
     * @return The Airport that the Plane should depart from
     */
    @Override
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    /**
     * @return The Airport that the Plane should arrive at
     */
    @Override
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }


    /**
     *
     * @param newAp Airport to be set as Arrival Airport
     */
    @Override
    public void setArrivalAirport(Airport newAp) {
        this.arrivalAirport = newAp;
    }

    /**
     *
     * @param newAp Airport to be set as Departure Airport
     */
    @Override
    public void setDepartureAirport(Airport newAp) {
        this.departureAirport = newAp;
    }

    @Override
    public boolean getEnabled() {
        return rteEnabled;
    }

    @Override
    public void enableRoute() {
        this.rteEnabled = true;
    }

    @Override
    public void disableRoute() {
        this.rteEnabled = false;
    }

    @Override
    public void toggleEnable() {
        if(this.rteEnabled){
            disableRoute();;
        }else{
            enableRoute();
        };
    }

    @Override
    public String toString(){

        return "Route from: "+ departureAirport.toString() +" -> "+ arrivalAirport.toString();
    }
}
