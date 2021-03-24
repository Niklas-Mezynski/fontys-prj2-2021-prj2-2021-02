package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.SalesManager;

import java.util.Objects;

public class SalesManagerImpl implements SalesManager {
    private final String name;
    private final String email;
    private final String password;

    public SalesManagerImpl(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }
}
