package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.dao.Savable;
import com.g02.flightsalesfx.businessEntities.Airport;

@TableName("airports")
public class AirportImpl implements Airport, Savable {

    @PrimaryKey
    public String name;
    public String city;
    public String country;

    public AirportImpl(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
    }

    private AirportImpl() {

    }

    public static AirportImpl of(Airport a) {
        return new AirportImpl(a.getName(), a.getCity(), a.getCountry());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirportImpl airport = (AirportImpl) o;

        if (getName() != null ? !getName().equals(airport.getName()) : airport.getName() != null) return false;
        if (getCity() != null ? !getCity().equals(airport.getCity()) : airport.getCity() != null) return false;
        return getCountry() != null ? getCountry().equals(airport.getCountry()) : airport.getCountry() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }
}
