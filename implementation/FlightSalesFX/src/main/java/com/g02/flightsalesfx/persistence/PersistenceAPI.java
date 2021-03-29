package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessEntities.SeatManager;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;
import com.g02.flightsalesfx.businessLogic.PlaneManagerImpl;
import com.g02.flightsalesfx.businessLogic.SeatManagerImpl;

public interface PersistenceAPI {

    EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager);

    PlaneStorageService getPlaneStorageService(PlaneManager planeManager);

    SeatStorageService getSeatStorageService(SeatManager seatManager);

    SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager);
}
