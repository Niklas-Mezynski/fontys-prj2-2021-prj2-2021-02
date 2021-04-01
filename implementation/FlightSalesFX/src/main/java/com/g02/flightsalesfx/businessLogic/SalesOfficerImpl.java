package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.SalesOfficer;

import java.util.Objects;

public class SalesOfficerImpl implements SalesOfficer {
    private final String name;
    private final String email;
    private final String password;

    public SalesOfficerImpl(String name, String email, String password) {
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
        SalesOfficerImpl that = (SalesOfficerImpl) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }
}

