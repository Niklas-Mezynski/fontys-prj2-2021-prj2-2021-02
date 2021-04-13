package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.persistence.PersistenceAPIImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightImpl implements Flight {

    private int flightNumber;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private Route route;
    private Plane plane;
    private double price;
    private boolean salesProcessStarted = false;

    private SalesOfficer creator;
    private List<PriceReduction> reductionList = new ArrayList<>();
    private List<FlightOption> optionsList = new ArrayList<>();

    public FlightImpl(SalesOfficer creator, int fNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        this.creator = creator;
        flightNumber = fNumber;
        departure = dep;
        arrival = arr;
        this.route = route;
        this.plane = plane;
        this.price = price;

    }


    /**
     * Starts the Sales process for this Flight
     */
    @Override
    public String toString(){
        return "ID: "+flightNumber+"; "+ route+"; "+plane+"; takeoff: "+departure+", arrival: "+arrival+"; Price:"+price+"; sale started:"+salesProcessStarted;
    }
    @Override
    public void startSalesProcess() {
        salesProcessStarted = true;
    }

    /**
     * @return Flight number of this Flight
     */
    @Override
    public int getFlightNumber() {
        return this.flightNumber;
    }

    /**
     * @return Current Price of this Flight
     */
    @Override
    public double getPrice() {
        /*reductionList.stream()
                .forEach(priceReduction -> price = price*(1-priceReduction.getPercentageAsADouble()));
*/
        return price;
    }

    /**
     * @return Route of this Flight
     */
    @Override
    public Route getRoute() {
        return route;
    }

    /**
     * @return Whether the Sales Process has started for this Flight
     * true = started
     * false = not started yet
     */
    @Override
    public boolean getSalesProcessStatus() {
        return salesProcessStarted;
    }

    /**
     * @return The Date that this Plane should/has arrived
     */
    @Override
    public LocalDateTime getArrival() {
        return arrival;
    }

    /**
     * @return The Date that this Plane is due to/has departed
     */
    @Override
    public LocalDateTime getDeparture() {
        return departure;
    }

    /**
     * @return The SalesOfficer that scheduled this Flight
     */
    @Override
    public SalesOfficer getCreatedBy() {
        return creator;
    }

    /**
     * @return The List of PriceReductions that apply for this Flight
     */
    @Override
    public List<PriceReduction> getPriceReductions() {
        return reductionList;
    }

    @Override
    public PriceReduction applyPriceReduction(PriceReduction p) {
        reductionList.add(p);
        return p;
    }

    @Override
    public void setArrival(LocalDateTime newArrival) {
        this.arrival = newArrival;
    }

    @Override
    public void setDeparture(LocalDateTime newDeparture) {
        this.departure = newDeparture;
    }

    /*public boolean removePriceReduction(PriceReduction p) {
        if(reductionList.contains(p)) {
            reductionList.remove(p);
            price = price * (1 + p.getPercentageAsADouble());
        }
        return false;
    }*/

    /**
     * @return The Plane that this Flight uses
     */
    @Override
    public Plane getPlane() {
        return plane;
    }

    /**
     * @return The List of FlightOptions that are available on this Flight
     */
    @Override
    public List<FlightOption> getFlightOptions() {
        return optionsList;
    }

    @Override
    public void addFlightOption(FlightOption flightOption) {
        this.optionsList.add(flightOption);
    }

    @Override
    public void addAllFlightOptions(List<? extends FlightOption> options) {
        this.optionsList.addAll(options);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightImpl flight = (FlightImpl) o;

        if (getFlightNumber() != flight.getFlightNumber()) return false;
        if (Double.compare(flight.getPrice(), getPrice()) != 0) return false;
        if (salesProcessStarted != flight.salesProcessStarted) return false;
        if (getDeparture() != null ? !getDeparture().equals(flight.getDeparture()) : flight.getDeparture() != null)
            return false;
        if (getArrival() != null ? !getArrival().equals(flight.getArrival()) : flight.getArrival() != null)
            return false;
        if (getRoute() != null ? !getRoute().equals(flight.getRoute()) : flight.getRoute() != null) return false;
        if (getPlane() != null ? !getPlane().equals(flight.getPlane()) : flight.getPlane() != null) return false;
        if (creator != null ? !creator.equals(flight.creator) : flight.creator != null) return false;
        if (reductionList != null ? !reductionList.equals(flight.reductionList) : flight.reductionList != null)
            return false;
        return optionsList != null ? optionsList.equals(flight.optionsList) : flight.optionsList == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFlightNumber();
        result = 31 * result + (getDeparture() != null ? getDeparture().hashCode() : 0);
        result = 31 * result + (getArrival() != null ? getArrival().hashCode() : 0);
        result = 31 * result + (getRoute() != null ? getRoute().hashCode() : 0);
        result = 31 * result + (getPlane() != null ? getPlane().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (salesProcessStarted ? 1 : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (reductionList != null ? reductionList.hashCode() : 0);
        result = 31 * result + (optionsList != null ? optionsList.hashCode() : 0);
        return result;
    }
}
