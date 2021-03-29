package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.EmployeeManager;
import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessEntities.PlaneManager;
import com.g02.flightsalesfx.businessEntities.SeatManager;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;
import com.g02.flightsalesfx.businessLogic.SeatManagerImpl;

public class PersistenceAPIImpl implements PersistenceAPI, PersistenceApiImplementationProvider {

    private EmployeeStorageServiceImpl employeeStorageService;
    private PlaneStorageService planeStorageService;
    private SeatStorageService seatStorageService;
    private SeatOptionsStorageServiceImpl seatOptionStorageService;

    @Override
    public EmployeeStorageService getEmployeeStorageService(EmployeeManager employeeManager) {
        if (employeeStorageService == null)
            employeeStorageService = new EmployeeStorageServiceImpl(employeeManager);
        return employeeStorageService;
    }

    @Override
    public PlaneStorageService getPlaneStorageService(PlaneManager planeManager) {
        if (planeStorageService == null) {
            planeStorageService = new PlaneStorageServiceImpl(planeManager);
        }
        return planeStorageService;
    }

    @Override
    public SeatStorageService getSeatStorageService(SeatManager seatManager) {
        if (seatStorageService == null) {
            seatStorageService = new SeatStorageServiceImpl(seatManager);
        }
        return seatStorageService;
    }

    @Override
    public SeatOptionsStorageService getSeatOptionStorageService(OptionManager optionManager) {
        if (seatOptionStorageService == null) {
            seatOptionStorageService = new SeatOptionsStorageServiceImpl(optionManager);
        }
        return seatOptionStorageService;
    }
}
