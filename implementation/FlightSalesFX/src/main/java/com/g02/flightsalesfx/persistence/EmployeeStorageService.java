package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeStorageService {

    Employee add(Employee employee);

    List<Employee> getAll();

    Optional<? extends Employee> get(String email);
}
