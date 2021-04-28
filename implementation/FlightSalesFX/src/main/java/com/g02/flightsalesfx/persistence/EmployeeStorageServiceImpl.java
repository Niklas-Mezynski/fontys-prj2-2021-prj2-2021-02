package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.dao.Savable;
import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import com.g02.flightsalesfx.businessLogic.SalesManagerImpl;
import com.g02.flightsalesfx.businessLogic.SalesOfficerImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<? extends Employee> ret= Optional.empty();
        try {
            if (employee instanceof SalesEmployee) {
                var insertedemployee = new SalesEmployeeImpl(employee.getName(), employee.getEmail(), employee.getPassword());
                ret = sedao.insert(insertedemployee);
            } else if (employee instanceof SalesOfficer) {
                var insertedemployee = new SalesOfficerImpl(employee.getName(), employee.getEmail(), employee.getPassword());
                ret = sodao.insert(insertedemployee);
            } else if (employee instanceof SalesManager) {
                var insertedemployee = new SalesManagerImpl(employee.getName(), employee.getEmail(), employee.getPassword());
                ret = smdao.insert(insertedemployee);
            } else return null; //Invalid Employee Type
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ret.isPresent()? ret.get() :null;

    }

    @Override
    public List<Employee> getAll() {
        var ret=new ArrayList<Employee>();
        try {
            ret.addAll(sedao.getAll());
            ret.addAll(sodao.getAll());
            ret.addAll(smdao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Optional<? extends Employee> get(String email){
        Optional<? extends Employee> opt;
        try {
            opt=sedao.get(new Object[]{email});
            if(opt.isPresent())return opt;
            opt=sodao.get(new Object[]{email});
            if(opt.isPresent())return opt;
            opt=smdao.get(new Object[]{email});
            return opt;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
