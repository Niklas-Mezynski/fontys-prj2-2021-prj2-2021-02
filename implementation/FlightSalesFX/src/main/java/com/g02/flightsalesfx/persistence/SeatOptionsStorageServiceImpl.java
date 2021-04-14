package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anato
 */
public class SeatOptionsStorageServiceImpl implements SeatOptionsStorageService{
    
    private final List<SeatOption> seatOptions;
    
    public SeatOptionsStorageServiceImpl(OptionManager optionMgr){
        seatOptions=new ArrayList<>();
        add(optionMgr.createSeatOption("SeatOption1", 20.5));
        add(optionMgr.createSeatOption("SeatOption2", 50));
    }

    @Override
    public void add(SeatOption s) {
        seatOptions.add(s);
    }

    @Override
    public List<SeatOption> getAll() {
        return seatOptions;
    }
    
}
