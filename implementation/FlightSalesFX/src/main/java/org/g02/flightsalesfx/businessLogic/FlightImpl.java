package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.Ignore;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@TableName("flights")
public class FlightImpl implements Flight, Savable {

    @PrimaryKey(autogen = true)
    public int flightNumber;
    public LocalDateTime departure;
    public LocalDateTime arrival;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.RouteImpl")
    public RouteImpl route;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.PlaneImpl")
    public PlaneImpl plane;
    public double price;
    public boolean salesProcessStarted = false;

    @ForeignKey("com.g02.flightsalesfx.businessLogic.SalesOfficerImpl")
    public SalesOfficerImpl creator;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl")
    public StaticPriceReductionImpl[] staticPriceReductions = new StaticPriceReductionImpl[0];
    @ForeignKey("com.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl")
    public DynamicPriceReductionImpl[] dynamicPriceReductions = new DynamicPriceReductionImpl[0];
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightOptionImpl")
    private FlightOptionImpl[] optionsListDB = new FlightOptionImpl[0];
    @Ignore
    private List<FlightOptionImpl> optionsList;

    public FlightImpl(SalesOfficerImpl creator, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        this.creator = creator;
        departure = dep;
        arrival = arr;
        this.route = RouteImpl.of(route);
        this.plane = PlaneImpl.of(plane);
        this.price = price;
        optionsList=new ArrayList<>();
    }

    public FlightImpl(SalesOfficerImpl creator, int flightNumber, LocalDateTime dep, LocalDateTime arr, Route route, Plane plane, double price) {
        this.creator = creator;
        this.flightNumber = flightNumber;
        departure = dep;
        arrival = arr;
        this.route = RouteImpl.of(route);
        this.plane = PlaneImpl.of(plane);
        this.price = price;
        optionsList=new ArrayList<>();
    }

    public static FlightImpl of(Flight f) {
        var ret= new FlightImpl(SalesOfficerImpl.of(f.getCreatedBy()),f.getFlightNumber(), f.getDeparture(), f.getArrival(), f.getRoute(), f.getPlane(), f.getPrice());
        ret.addAllFlightOptions(f.getFlightOptions());
        return ret;
    }

    private FlightImpl() {}

    /**
     * Starts the Sales process for this Flight
     */
    @Override
    public String toString() {
        return "ID: " + flightNumber + "; " + route + "; " + plane + "; takeoff: " + departure + ", arrival: " + arrival + "; Price:" + price + "; sale started:" + salesProcessStarted;
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

    @Override
    public void setArrival(LocalDateTime newArrival) {
        this.arrival = newArrival;
    }

    /**
     * @return The Date that this Plane is due to/has departed
     */
    @Override
    public LocalDateTime getDeparture() {
        return departure;
    }

    @Override
    public void setDeparture(LocalDateTime newDeparture) {
        this.departure = newDeparture;
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
        List<PriceReduction> staticPriceReductions = Arrays.asList(this.staticPriceReductions);
        List<PriceReduction> dynamicPriceReductions = Arrays.asList(this.dynamicPriceReductions);
        staticPriceReductions.addAll(dynamicPriceReductions);
        return staticPriceReductions;
    }

    @Override
    public void addPriceReduction(PriceReduction p) {
        if (p instanceof DynamicPriceReductionImpl) {
            var dynamicPriceReductions = Arrays.asList(this.dynamicPriceReductions);
            dynamicPriceReductions.add((DynamicPriceReductionImpl) p);
            this.dynamicPriceReductions = dynamicPriceReductions.toArray(new DynamicPriceReductionImpl[0]);
        } else if (p instanceof StaticPriceReductionImpl) {
            var staticPriceReductions = Arrays.asList(this.staticPriceReductions);
            staticPriceReductions.add((StaticPriceReductionImpl) p);
            this.staticPriceReductions = staticPriceReductions.toArray(new StaticPriceReductionImpl[0]);
        }
    }

    /*public boolean removePriceReduction(PriceReduction p) {
        if(reductionList.contains(p)) {
            reductionList.remove(p);
            price = price * (1 + p.getPercentageAsADouble());
        }
        return false;
    }*/

    /**
     * @return Current Price of this Flight
     */
    @Override
    public double getPriceWithPriceReductionsApplied() {
        List<PriceReduction> availablePriceReductions = Arrays.stream(this.staticPriceReductions)
                .filter(priceReduction -> priceReduction.getEndDate().isAfter(LocalDate.now()))
                .sorted()
                .collect(Collectors.toList());
        List<PriceReduction> collect = Arrays.stream(dynamicPriceReductions)
                .filter(priceReduction -> priceReduction.getEndDate().isAfter(LocalDate.now()))
                .sorted()
                .collect(Collectors.toList());
        availablePriceReductions.addAll(collect);
        double newPrice = this.price;
        for (PriceReduction pr : availablePriceReductions) {
            newPrice = newPrice - (newPrice * pr.getPercentageAsDouble());
        }
        return newPrice;
    }

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
        return new ArrayList<>(optionsList);
    }

    @Override
    public void addFlightOption(FlightOption flightOption) {
        optionsList.add(FlightOptionImpl.of(flightOption));
    }

    @Override
    public void addAllFlightOptions(List<? extends FlightOption> options) {
        var tmp=options.stream().map(FlightOptionImpl::of).collect(Collectors.toList());
        optionsList.addAll(tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightImpl flight = (FlightImpl) o;
        return getFlightNumber() == flight.getFlightNumber() && Double.compare(flight.getPrice(), getPrice()) == 0 && salesProcessStarted == flight.salesProcessStarted && Objects.equals(getDeparture(), flight.getDeparture()) && Objects.equals(getArrival(), flight.getArrival()) && Objects.equals(getRoute(), flight.getRoute()) && Objects.equals(getPlane(), flight.getPlane()) && Objects.equals(creator, flight.creator) && Arrays.equals(staticPriceReductions, flight.staticPriceReductions) && Arrays.equals(dynamicPriceReductions, flight.dynamicPriceReductions) && optionsList.equals(flight.optionsList);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getFlightNumber(), getDeparture(), getArrival(), getRoute(), getPlane(), getPrice(), salesProcessStarted, creator);
        result = 31 * result + Arrays.hashCode(staticPriceReductions);
        result = 31 * result + Arrays.hashCode(dynamicPriceReductions);
        result = 31 * result + optionsList.hashCode();
        return result;
    }
    private void beforeDeconstruction(){
        optionsListDB=optionsList.toArray(FlightOptionImpl[]::new);
    }
    private void afterConstruction(){
        optionsList= Arrays.stream(optionsListDB).collect(Collectors.toList());
    }
}
