package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReoccurringFlightImpl implements ReoccurringFlight  {

    private Flight flight;
    private int interval;

    public ReoccurringFlightImpl(Flight flight, int interval) {
        this.flight = flight;
        this.interval = interval;
    }

    // Amount of days until this flight reoccurrs;
    @Override
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
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
    public LocalDateTime getDeparture() {
        return flight.getDeparture();
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

    @Override
    public void setArrival(LocalDateTime newArrival) {
        flight.setArrival(newArrival);
    }

    @Override
    public void setDeparture(LocalDateTime newDeparture) {
        flight.setDeparture(newDeparture);
    }

    @Override
    public void addFlightOption(FlightOption flightOption) {
        flight.addFlightOption(flightOption);
    }

    @Override
    public void addAllFlightOptions(List<? extends FlightOption> options) {
        flight.addAllFlightOptions(options);
    }
}
