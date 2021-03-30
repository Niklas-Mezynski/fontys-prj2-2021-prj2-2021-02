/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.OptionManager;
import com.g02.flightsalesfx.businessEntities.SeatOption;
import com.g02.flightsalesfx.businessLogic.OptionManagerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anato
 */
public class SeatOptionsStorageServiceImpl implements SeatOptionsStorageService{
    
    private List<SeatOption> seatOptions;
    
    public SeatOptionsStorageServiceImpl(OptionManager optionMgr){
        seatOptions=new ArrayList<>();
        add(optionMgr.createSeatOption("SeatOption1"));
        add(optionMgr.createSeatOption("SeatOption2"));
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
