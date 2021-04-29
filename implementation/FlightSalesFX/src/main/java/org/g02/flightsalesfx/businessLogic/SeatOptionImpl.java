package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.SeatOption;

@TableName("seatoptions")
public class SeatOptionImpl implements SeatOption, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public String name;
    public double price;

    public SeatOptionImpl (String name, Double price) {
        this.name = name;
        this.price = price;
    }
    private SeatOptionImpl(){}

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

        if (Double.compare(that.getPrice(), getPrice()) != 0) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName() != null ? getName().hashCode() : 0;
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    public static SeatOptionImpl of(SeatOption s){
        return new SeatOptionImpl(s.getName(),s.getPrice());
    }
}
