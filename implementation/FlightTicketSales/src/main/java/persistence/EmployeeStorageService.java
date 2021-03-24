package persistence;

import businessEntities.Employee;

import java.util.List;

public interface EmployeeStorageService {

    void add(Employee employee);

    List<Employee> getAll();

}
