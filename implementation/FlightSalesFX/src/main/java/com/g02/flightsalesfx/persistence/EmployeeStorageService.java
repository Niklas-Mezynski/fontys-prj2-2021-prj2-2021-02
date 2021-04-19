package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Employee;

import java.util.List;

public interface EmployeeStorageService {

    Employee add(Employee employee);

    List<Employee> getAll();

}
