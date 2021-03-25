package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.persistence.EmployeeStorageService;
import com.g02.flightsalesfx.persistence.PersistenceAPI;

import java.util.Optional;

public class BusinessLogicAPIImpl implements BusinessLogicAPI {
    final PersistenceAPI persistenceAPI;

    public BusinessLogicAPIImpl(PersistenceAPI persistenceAPI) {
        this.persistenceAPI = persistenceAPI;
    }

    @Override
    public EmployeeManager getEmployeeManager() {
        EmployeeManagerImpl employeeManager = new EmployeeManagerImpl();
        employeeManager.setEmployeeStorageService(persistenceAPI.getEmployeeStorageService(employeeManager));
        return employeeManager;
    }

    @Override
    public Employee login(String email, String password) {
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(getEmployeeManager());
        var first = employeeStorageService.getAll().stream().filter(employee -> employee.getEmail().equals(email) && employee.getPassword().equals(password)).findFirst();
        if (first.isEmpty()) {
            return null;
        }
        return first.get();
    }
}
