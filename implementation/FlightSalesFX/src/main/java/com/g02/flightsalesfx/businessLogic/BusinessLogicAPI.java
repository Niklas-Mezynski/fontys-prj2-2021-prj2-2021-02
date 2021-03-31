package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessEntities.SeatManager;
import com.g02.flightsalesfx.businessEntities.OptionManager;

public interface BusinessLogicAPI {

    public EmployeeManager getEmployeeManager();

    public PlaneManager getPlaneManager();

    public SeatManager getSeatManager();

    public OptionManager getOptionManager();

    public Employee login(String email, String password);

    public boolean createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats);

}
