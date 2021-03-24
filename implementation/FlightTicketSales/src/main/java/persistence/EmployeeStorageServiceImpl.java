package persistence;

import businessEntities.Employee;
import businessEntities.EmployeeManager;
import businessEntities.SalesEmployee;
import businessEntities.SalesOfficer;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStorageServiceImpl implements EmployeeStorageService {

    private final EmployeeManager employeeManager;

    private List<Employee> employees;

    public EmployeeStorageServiceImpl(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
        employees = new ArrayList<Employee>();
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
