package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessEntities.SeatManager;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.persistence.EmployeeStorageService;
import com.g02.flightsalesfx.persistence.PersistenceAPI;

import java.util.Optional;

public class BusinessLogicAPIImpl implements BusinessLogicAPI {
    final PersistenceAPI persistenceAPI;
    private EmployeeManagerImpl employeeManager;
    private PlaneManagerImpl planeManager;
    private SeatManagerImpl seatManager;
    private OptionManagerImpl optionManager;

    public BusinessLogicAPIImpl(PersistenceAPI persistenceAPI) {
        this.persistenceAPI = persistenceAPI;
    }

    @Override
    public EmployeeManager getEmployeeManager() {
        if (employeeManager == null) {
            employeeManager = new EmployeeManagerImpl();
            employeeManager.setEmployeeStorageService(persistenceAPI.getEmployeeStorageService(employeeManager));
        }
        return employeeManager;
    }

    @Override
    public PlaneManager getPlaneManager() {
        if (planeManager == null) {
            planeManager = new PlaneManagerImpl();
            planeManager.setPlaneStorageService(persistenceAPI.getPlaneStorageService(planeManager));
        }
        return planeManager;
    }

    @Override
    public SeatManager getSeatManager() {
        if (seatManager == null) {
            seatManager = new SeatManagerImpl();
            seatManager.setSeatStorageService(persistenceAPI.getSeatStorageService(seatManager));
        }
        return seatManager;
    }

    @Override
    public OptionManager getOptionManager() {
        if (optionManager == null) {
            optionManager = new OptionManagerImpl();
            optionManager.setSeatOptionStorageService(persistenceAPI.getSeatOptionStorageService(optionManager));
        }
        return optionManager;
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
