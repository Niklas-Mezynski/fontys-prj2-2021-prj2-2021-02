package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import com.g02.flightsalesfx.businessLogic.SalesManagerImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeStorageServiceImpl implements EmployeeStorageService {

    private final EmployeeManager employeeManager;

    private final Dao<SalesEmployeeImpl> sedao;
    private final Dao<SalesOfficerImpl> sodao;
    private final Dao<SalesManagerImpl> smdao;

    public EmployeeStorageServiceImpl(EmployeeManager employeeManager, Dao<SalesEmployeeImpl> sedao, Dao<SalesOfficerImpl> sodao, Dao<SalesManagerImpl> smdao) {
        this.employeeManager = employeeManager;
        this.sedao = sedao;
        this.sodao = sodao;
        this.smdao = smdao;

    }

    @Override
    public Employee add(Employee employee) {
        Dao<? extends Employee> dao=sedao;
        if (employee instanceof SalesEmployeeImpl) dao=sedao;
        if (employee instanceof SalesOfficerImpl) dao=sodao;
        if (employee instanceof SalesManagerImpl) dao=smdao;
        try {
            var ret= dao.insert((Savable) employee);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> getAll() {
        var ret=new ArrayList<Employee>();
        try {
            ret.addAll(sedao.getAll());
            ret.addAll(sodao.getAll());
            ret.addAll(smdao.getAll());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
