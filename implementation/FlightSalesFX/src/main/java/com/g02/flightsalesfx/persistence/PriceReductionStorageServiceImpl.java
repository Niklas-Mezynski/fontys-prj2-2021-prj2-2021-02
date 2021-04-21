package com.g02.flightsalesfx.persistence;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.PriceReduction;
import com.g02.flightsalesfx.businessEntities.PriceReductionManager;
import com.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl;
import com.g02.flightsalesfx.businessLogic.PriceReductionImpl;
import com.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceReductionStorageServiceImpl implements PriceReductionStorageService{

    private final Dao<StaticPriceReductionImpl> sdao;
    private final Dao<DynamicPriceReductionImpl> ddao;

    public PriceReductionStorageServiceImpl(PriceReductionManager priceReductionManager, Dao<StaticPriceReductionImpl> sdao, Dao<DynamicPriceReductionImpl> ddao) {
        this.sdao = sdao;
        this.ddao = ddao;
    }

    @Override
    public PriceReduction add(PriceReduction priceReduction) {
        var dao=(Dao<? extends PriceReduction>)sdao;
        if(priceReduction instanceof StaticPriceReductionImpl) {
            dao=sdao;
        }else if(priceReduction instanceof DynamicPriceReductionImpl) {
            dao=ddao;
        }else {
            priceReduction=new StaticPriceReductionImpl(priceReduction.getName(),priceReduction.getEndDate() , priceReduction.getPercentageAsDouble()); //Default if not Impl already
        }
        try {
            var ret= dao.insert((Savable) priceReduction);
            return ret.size()>0?ret.get(0):null;
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PriceReduction> getAll() {
        var ret=new ArrayList<PriceReduction>();
        try {
            ret.addAll(sdao.getAll());
            ret.addAll(ddao.getAll());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
