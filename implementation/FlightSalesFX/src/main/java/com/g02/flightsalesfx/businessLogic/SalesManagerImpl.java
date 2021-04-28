package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.dao.Savable;
import com.g02.flightsalesfx.businessEntities.SalesManager;

import java.util.Objects;

@TableName("salesmanagers")
public class SalesManagerImpl implements SalesManager, Savable {
    public final String name;
    @PrimaryKey
    public final String email;
    public final String password;

    public SalesManagerImpl(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    private SalesManagerImpl(){
        this(null,null,null);
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
        SalesManagerImpl that = (SalesManagerImpl) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }
}
