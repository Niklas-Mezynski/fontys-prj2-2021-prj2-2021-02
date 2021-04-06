package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;

public class RouteImpl implements Route {
    private  Airport departureAirport;
    private  Airport arrivalAirport;

    public RouteImpl (Airport departureAirport, Airport arrivalAirport) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
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
}
