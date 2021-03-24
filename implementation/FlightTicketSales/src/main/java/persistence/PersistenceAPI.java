package persistence;

import businessEntities.EmployeeManager;

public interface PersistenceAPI {

    default EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        return null;
    }

}
