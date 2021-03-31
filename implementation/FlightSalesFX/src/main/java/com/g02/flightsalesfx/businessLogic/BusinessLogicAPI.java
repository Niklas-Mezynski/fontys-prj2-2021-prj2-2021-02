package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.CreatePlaneController;
import com.g02.flightsalesfx.businessEntities.*;

import java.util.List;

public interface BusinessLogicAPI {

    public EmployeeManager getEmployeeManager();

    public PlaneManager getPlaneManager();

    public SeatManager getSeatManager();

    public OptionManager getOptionManager();

    public Employee login(String email, String password);

    public boolean createPlaneFromUI(String name, String type, String manufacturer, List<Seat> seats);

}
