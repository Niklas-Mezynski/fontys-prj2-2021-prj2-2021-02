/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g02.flightsalesfx.persistence;

import com.g02.flightsalesfx.businessEntities.SeatOption;
import java.util.List;

/**
 *
 * @author anato
 */
public interface SeatOptionsStorageService {
    void add(SeatOption s);
    
    List<SeatOption> getAll();
}
