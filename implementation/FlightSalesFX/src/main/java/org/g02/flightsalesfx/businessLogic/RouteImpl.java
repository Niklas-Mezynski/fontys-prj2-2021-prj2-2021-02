package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.*;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.Route;

import java.util.Objects;

@TableName("routes")
public class RouteImpl implements Route, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    @FieldName("enabled")
    public boolean rteEnabled;
    private AirportImpl arrivalAirport;
    private AirportImpl departureAirport;

    public RouteImpl(Airport departureAirport, Airport arrivalAirport) {
        this.departureAirport = AirportImpl.of(departureAirport);
        this.arrivalAirport = AirportImpl.of(arrivalAirport);
        this.rteEnabled = true;
    }

    public RouteImpl(int id, Airport departureAirport, Airport arrivalAirport, boolean rteEnabled) {
        this(departureAirport, arrivalAirport);
        this.id = id;
        this.rteEnabled = rteEnabled;
    }

    private RouteImpl() {

    }

    public static RouteImpl of(Route r) {
        return r == null ? null : new RouteImpl(r.getId(), r.getDepartureAirport(), r.getArrivalAirport(), r.getEnabled());
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
        } else {
            enableRoute();
        }
    }

    @Override
    public int getId() {
        return id;
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

}
