package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Airport;

public class AirportImpl implements Airport {

    private final String name;
    private final String city;
    private final String country;

    public AirportImpl(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
    }
    /**
     * @return Name of the airport
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return Name of the city that the airport is in
     */
    @Override
    public String getCity() {
        return this.city;
    }

    /**
     * @return Name of the country that the airport is in
     */
    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.city + ", " + this.country;
    }
}
