package persistence;

import businessEntities.EmployeeManager;

public class PersistenceAPIImpl implements PersistenceAPI, PersistenceApiImplementationProvider {

    private EmployeeStorageServiceImpl employeeStorageService;

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null)
            employeeStorageService = new EmployeeStorageServiceImpl(employeeManager);
        return employeeStorageService;
    }
}
