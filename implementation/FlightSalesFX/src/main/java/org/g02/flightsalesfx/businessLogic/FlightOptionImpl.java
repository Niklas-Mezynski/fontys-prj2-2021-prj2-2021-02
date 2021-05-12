package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.FlightOption;

@TableName("flightoptions")
public class FlightOptionImpl implements FlightOption, Savable {
    @PrimaryKey(autogen = true)
    public int id;
    public String name;
    public int maxAvailable;
    public int currentAvailable;
    public double price;

    private FlightOptionImpl(){}

    public FlightOptionImpl (String name, int maxAvailable, double price) {
        this(name,maxAvailable,price,-1,0);
    }
    public FlightOptionImpl (String name, int maxAvailable, double price, int currentAvailable,int id) {
        this.id=id;
        this.name = name;
        this.maxAvailable = maxAvailable;
        this.price = price;
        this.currentAvailable=currentAvailable;
    }
    public static FlightOptionImpl of(FlightOption f){
        return new FlightOptionImpl(f.getName(), f.getMaxAvailability(), f.getPrice(),f.getCurrentAvailability(),f.getID());
    }

    private FlightOptionImpl () {}

    /**
     * @return How often this FlightOption is currently available
     */
    @Override
    public int getCurrentAvailability() {
        //TODO (should this be saved or calculated by subtracting the already booked FOs from the maxAvailable)
        return currentAvailable;
    }

    /**
     * @return How often this FlightOption was available at the start of the Sales Process
     */
    @Override
    public int getMaxAvailability() {
        return maxAvailable;
    }

    @Override
    public double getPrice() {
        return price;
    }

    /**
     * @return The Name of this Option
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Max available: " + maxAvailable + ", Price: " + price + "€";
    }

    @Override
    public int getID() {
        return id;
    }
}
