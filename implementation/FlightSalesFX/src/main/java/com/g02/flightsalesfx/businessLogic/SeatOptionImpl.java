package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.Objects;

public class SeatOptionImpl implements SeatOption {

    private String name;
    private double price;

    public SeatOptionImpl (String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @return The price that this FlightOption costs
     */
    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "SeatOptionImpl{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatOptionImpl that = (SeatOptionImpl) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
