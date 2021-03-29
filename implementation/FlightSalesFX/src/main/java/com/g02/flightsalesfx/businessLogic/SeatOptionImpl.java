package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.SeatOption;

public class SeatOptionImpl implements SeatOption {

    private String name;

    public SeatOptionImpl (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "SeatOptionImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
