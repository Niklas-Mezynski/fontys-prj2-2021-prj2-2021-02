package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.*;

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
        SalesOfficer salesOfficerShort = employeeManager.createSalesOfficer("a", "a", "a");
        SalesManager salesManagerShort = employeeManager.createSalesManager("b", "b", "b");
        SalesEmployee salesEmployeeShort = employeeManager.createSalesEmployee("c", "c", "c");
        SalesOfficer justRet = employeeManager.createSalesOfficer("", "", "");
        employees.add(salesEmployee);
        employees.add(salesOfficer);
        employees.add(salesManagerShort);
        employees.add(salesOfficerShort);
        employees.add(salesEmployeeShort);
        employees.add(justRet);
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
