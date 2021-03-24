package businessEntities;

public interface EmployeeManager {

    public SalesManager createSalesManager(String name, String email, String password);

    public SalesEmployee createSalesEmployee(String name, String email, String password);

    public SalesOfficer createSalesOfficer(String name, String email, String password);

}
