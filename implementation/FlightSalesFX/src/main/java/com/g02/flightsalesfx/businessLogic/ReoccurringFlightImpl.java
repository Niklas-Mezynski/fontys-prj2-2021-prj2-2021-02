package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.dao.Savable;
import com.g02.flightsalesfx.businessEntities.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;

@TableName("reoccuringflights")
public class ReoccurringFlightImpl implements ReoccurringFlight, Savable {

    @PrimaryKey
    private int id;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.FlightImpl")
    public FlightImpl flight;
    @FieldName("interval")
    public long intervalDB;
    @Ignore
    public Duration interval;

    public ReoccurringFlightImpl(Flight flight, Duration interval) {
        this.flight = FlightImpl.of(flight);
        this.id=flight.getFlightNumber();
        this.interval = interval;
    }


    private void afterConstruction() {
        id=flight.getFlightNumber();
        interval = Duration.of(intervalDB, SECONDS);
    }

    // Amount of days until this flight reoccurrs;
    @Override
    public Duration getInterval() {
        return interval;
    }

    public void setInterval(Duration interval) {
        this.interval = interval;
        intervalDB = interval.get(SECONDS);
    }

    @Override
    public Flight getFlight() {
        return this.flight;
    }

    @Override
    public void startSalesProcess() {
        flight.startSalesProcess();
    }

    @Override
    public int getFlightNumber() {
        return flight.getFlightNumber();
    }

    @Override
    public double getPrice() {
        return flight.getPrice();
    }

    @Override
    public Route getRoute() {
        return flight.getRoute();
    }

    @Override
    public boolean getSalesProcessStatus() {
        return flight.getSalesProcessStatus();
    }

    @Override
    public LocalDateTime getArrival() {
        return flight.getArrival();
    }

    @Override
    public void setArrival(LocalDateTime newArrival) {
        flight.setArrival(newArrival);
    }

    @Override
    public LocalDateTime getDeparture() {
        return flight.getDeparture();
    }

    @Override
    public void setDeparture(LocalDateTime newDeparture) {
        flight.setDeparture(newDeparture);
    }

    @Override
    public SalesOfficer getCreatedBy() {
        return flight.getCreatedBy();
    }

    @Override
    public List<PriceReduction> getPriceReductions() {
        return flight.getPriceReductions();
    }

    @Override
    public Plane getPlane() {
        return flight.getPlane();
    }

    @Override
    public List<FlightOption> getFlightOptions() {
        return flight.getFlightOptions();
    }

    @Override
    public void addPriceReduction(PriceReduction p) {
        flight.addPriceReduction(p);
    }

    /**
     * @return Current Price of this Flight
     */
    @Override
    public double getPriceWithPriceReductionsApplied() {
        return flight.getPriceWithPriceReductionsApplied();
    }

    @Override
    public void addFlightOption(FlightOption flightOption) {
        flight.addFlightOption(flightOption);
    }

    @Override
    public void addAllFlightOptions(List<? extends FlightOption> options) {
        flight.addAllFlightOptions(options);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReoccurringFlightImpl that = (ReoccurringFlightImpl) o;
        return Objects.equals(getFlight(), that.getFlight()) && Objects.equals(getInterval(), that.getInterval());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlight(), getInterval());
    }
}
