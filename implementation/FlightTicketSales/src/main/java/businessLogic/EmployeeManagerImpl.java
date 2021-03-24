package businessLogic;

import businessEntities.EmployeeManager;
import businessEntities.SalesEmployee;
import businessEntities.SalesManager;
import businessEntities.SalesOfficer;

public class EmployeeManagerImpl implements EmployeeManager {
    @Override
    public SalesManager createSalesManager(String name, String email, String password) {
        return new SalesManagerImpl(name,email,password);
    }

    @Override
    public SalesEmployee createSalesEmployee(String name, String email, String password) {
        return new SalesEmployeeImpl(name,email,password);
    }

    @Override
    public SalesOfficer createSalesOfficer(String name, String email, String password) {
        return new SalesOfficerImpl(name,email,password);
    }


}
