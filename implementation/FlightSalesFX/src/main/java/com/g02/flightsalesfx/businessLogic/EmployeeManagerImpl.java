package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.SalesEmployee;
import com.g02.flightsalesfx.businessEntities.SalesManager;
import com.g02.flightsalesfx.businessEntities.SalesOfficer;
import com.g02.flightsalesfx.persistence.EmployeeStorageService;

public class EmployeeManagerImpl implements EmployeeManager {
    private EmployeeStorageService employeeStorageService;

    @Override
    public SalesManager createSalesManager(String name, String email, String password) {
        return new SalesManagerImpl(name,email,password);
    }

    @Override
    public SalesEmployee createSalesEmployee(String name, String email, String password) {
        return new SalesEmployeeImpl(name,email,password);
    }

    @Override
    public SalesOfficer createSalesOfficer(String name, String email, String password) {
        return new SalesOfficerImpl(name,email,password);
    }

    public void setEmployeeStorageService(EmployeeStorageService employeeStorageService) {
        this.employeeStorageService = employeeStorageService;
    }


}
