package org.g02.flightsalesfx.persistence;

import org.g02.btfdao.dao.Dao;
import org.g02.flightsalesfx.businessEntities.PriceReduction;
import org.g02.flightsalesfx.businessEntities.PriceReductionManager;
import org.g02.flightsalesfx.businessLogic.DynamicPriceReductionImpl;
import org.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<? extends PriceReduction> ret= Optional.empty();
        try {
            if (priceReduction instanceof StaticPriceReductionImpl) {
                ret = sdao.insert((StaticPriceReductionImpl) priceReduction);
            } else if (priceReduction instanceof DynamicPriceReductionImpl) {
                ret = ddao.insert((DynamicPriceReductionImpl) priceReduction);
            } else {
                priceReduction = new StaticPriceReductionImpl(priceReduction.getName(), priceReduction.getEndTime(),priceReduction.getStartTime(),priceReduction.isPercentage(), priceReduction.getPercentageAsDouble()); //Default if not Impl already
                ret = sdao.insert((StaticPriceReductionImpl) priceReduction);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ret.isPresent()?ret.get():null;
    }

    @Override
    public List<PriceReduction> getAll() {
        var ret=new ArrayList<PriceReduction>();
        try {
            ret.addAll(sdao.getAll());
            ret.addAll(ddao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
