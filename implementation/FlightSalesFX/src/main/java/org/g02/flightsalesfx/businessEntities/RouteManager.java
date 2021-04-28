/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g02.flightsalesfx.businessEntities;


/**
 *
 * @author anato
 */
public interface RouteManager {
    public Route createRoute(Airport departure, Airport arrival);

    public void editRoute(Route toEdit, Airport newDep, Airport newArr);
}
