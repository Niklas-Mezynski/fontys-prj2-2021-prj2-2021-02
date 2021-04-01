package com.g02.flightsalesfx.businessEntities;

public interface EmployeeManager {

    /**
     * @param name Name of the new Sales Manager Object
     * @param email Name of the new Sales Manager Object
     * @param password Name of the new Sales Manager Object
     * @return A new SalesManager Object
     */
    public SalesManager createSalesManager(String name, String email, String password);
    /**
     * @param name Name of the new Sales Employee Object
     * @param email Email of the new Sales Employee Object
     * @param password Password of the new Sales Employee Object
     * @return A new SalesEmployee Object
     */
    public SalesEmployee createSalesEmployee(String name, String email, String password);
    /**
     * @param name Name of the new Sales Officer Object
     * @param email Email of the new Sales Officer Object
     * @param password Password of the new Sales Officer Object
     * @return A new SalesOfficer Object
     */
    public SalesOfficer createSalesOfficer(String name, String email, String password);

}
