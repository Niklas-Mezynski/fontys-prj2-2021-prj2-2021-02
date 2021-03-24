package businessEntities;

import businessLogic.EmployeeManagerImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import persistence.EmployeeStorageService;
import persistence.PersistenceAPI;
import persistence.PersistenceAPIImpl;
import persistence.PersistenceApiImplementationProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeStorageServiceTest {

    PersistenceAPI persistenceAPI = PersistenceApiImplementationProvider.getImplementation();
    EmployeeManager employeeManager = new EmployeeManagerImpl();

    @Test
    void testGetAll() {
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(employeeManager);
        var all = employeeStorageService.getAll();
        SalesEmployee salesEmployee = employeeManager.createSalesEmployee("Peter", "peter@gmx.de", "peterIstDerBeste");
        SalesOfficer salesOfficer = employeeManager.createSalesOfficer("Franz", "franz@gmx.de", "franzIstDerBeste");
        assertThat(all).contains(salesEmployee, salesOfficer);
    }

    @Test
    void testAdd() {
        var employeeStorageService = persistenceAPI.getEmployeeStorageService(employeeManager);
        SalesManager salesManager = employeeManager.createSalesManager("Klaus", "klaus@gmx.de", "klausIstDerBeste");
        employeeStorageService.add(salesManager);
        assertThat(employeeStorageService.getAll()).contains(salesManager);
    }
}
