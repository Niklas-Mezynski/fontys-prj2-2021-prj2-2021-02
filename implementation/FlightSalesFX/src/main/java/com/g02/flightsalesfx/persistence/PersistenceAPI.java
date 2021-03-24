package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.EmployeeManager;

public interface PersistenceAPI {

    default EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        return null;
    }

}
