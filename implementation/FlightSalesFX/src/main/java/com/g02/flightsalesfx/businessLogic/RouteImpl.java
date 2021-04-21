package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Route;

import java.util.Objects;

@TableName("routes")
public class RouteImpl implements Route, Savable {

    @PrimaryKey
    public int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.AirportImpl")
    public AirportImpl[] airports = new AirportImpl[2];
    @Ignore
    private Airport arrivalAirport;
    @Ignore
    private Airport departureAirport;
    @FieldName("enabled")
    public boolean rteEnabled;

    public RouteImpl(Airport departureAirport, Airport arrivalAirport) {
        this.departureAirport = departureAirport;
        this.airports[0] = (AirportImpl) departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.airports[1] = (AirportImpl) arrivalAirport;
        this.rteEnabled = true;
    }

    public RouteImpl(int id,Airport departureAirport, Airport arrivalAirport, boolean rteEnabled) {
        this(departureAirport, arrivalAirport);
        this.id=id;
        this.rteEnabled = rteEnabled;
    }
    private RouteImpl(int id, boolean rteEnabled){
        this.id=id;
        this.rteEnabled=rteEnabled;
    }

    /**
     * @return The Airport that the Plane should depart from
     */
    @Override
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    /**
     * @param newAp Airport to be set as Departure Airport
     */
    @Override
    public void setDepartureAirport(Airport newAp) {
        this.departureAirport = (AirportImpl) newAp;
    }

    /**
     * @return The Airport that the Plane should arrive at
     */
    @Override
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * @param newAp Airport to be set as Arrival Airport
     */
    @Override
    public void setArrivalAirport(Airport newAp) {
        this.arrivalAirport = (AirportImpl) newAp;
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
        if (this.rteEnabled) {
            disableRoute();
            ;
        } else {
            enableRoute();
        }
        ;
    }

    @Override
    public String toString() {

        return "Route from: " + departureAirport.toString() + " -> " + arrivalAirport.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteImpl route = (RouteImpl) o;

        if (rteEnabled != route.rteEnabled) return false;
        if (!Objects.equals(departureAirport, route.departureAirport))
            return false;
        return Objects.equals(arrivalAirport, route.arrivalAirport);
    }

    @Override
    public int hashCode() {
        int result = departureAirport != null ? departureAirport.hashCode() : 0;
        result = 31 * result + (arrivalAirport != null ? arrivalAirport.hashCode() : 0);
        result = 31 * result + (rteEnabled ? 1 : 0);
        return result;
    }

    @Override
    public void afterConstruction() {
        arrivalAirport=airports[0];
        departureAirport=airports[1];
    }
}
