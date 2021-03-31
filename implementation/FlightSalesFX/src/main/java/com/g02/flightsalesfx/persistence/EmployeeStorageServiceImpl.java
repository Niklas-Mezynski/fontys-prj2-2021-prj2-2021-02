package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.SalesEmployee;
import com.g02.flightsalesfx.businessEntities.SalesOfficer;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStorageServiceImpl implements EmployeeStorageService {

    private final EmployeeManager employeeManager;

    private List<Employee> employees;

    public EmployeeStorageServiceImpl(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
        employees = new ArrayList<>();
        SalesEmployee salesEmployee = employeeManager.createSalesEmployee("Peter", "peter@gmx.de", "peterIstDerBeste");
        SalesOfficer salesOfficer = employeeManager.createSalesOfficer("Franz", "franz@gmx.de", "franzIstDerBeste");
        employees.add(salesEmployee);
        employees.add(salesOfficer);
    }

    @Override
    public void add(Employee employee) {
        employees.add(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employees;
    }
}
