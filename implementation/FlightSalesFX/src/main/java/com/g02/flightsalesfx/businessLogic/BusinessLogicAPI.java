package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.EmployeeManager;

public interface BusinessLogicAPI {

    public EmployeeManager getEmployeeManager();

    public Employee login(String email, String password);

}
