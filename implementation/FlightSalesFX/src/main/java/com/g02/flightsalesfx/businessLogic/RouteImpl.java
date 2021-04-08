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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteImpl route = (RouteImpl) o;

        if (rteEnabled != route.rteEnabled) return false;
        if (departureAirport != null ? !departureAirport.equals(route.departureAirport) : route.departureAirport != null)
            return false;
        return arrivalAirport != null ? arrivalAirport.equals(route.arrivalAirport) : route.arrivalAirport == null;
    }

    @Override
    public int hashCode() {
        int result = departureAirport != null ? departureAirport.hashCode() : 0;
        result = 31 * result + (arrivalAirport != null ? arrivalAirport.hashCode() : 0);
        result = 31 * result + (rteEnabled ? 1 : 0);
        return result;
    }
}
