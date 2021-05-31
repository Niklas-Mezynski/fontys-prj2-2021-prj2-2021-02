package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.SalesEmployee;

import java.util.Objects;

@TableName("salesemployees")
public class SalesEmployeeImpl implements SalesEmployee, Savable {
    public final String name;
    @PrimaryKey
    public final String email;
    public final String password;

    public SalesEmployeeImpl(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    private SalesEmployeeImpl(){
        this(null,null,null); //Will be overriden
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesEmployeeImpl that = (SalesEmployeeImpl) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    public static SalesEmployeeImpl of(SalesEmployee se){
        return new SalesEmployeeImpl(se.getName(), se.getEmail(), se.getPassword());
    }

    @Override
    public String toString() {
        return getName() + ", " + getEmail();
    }
}