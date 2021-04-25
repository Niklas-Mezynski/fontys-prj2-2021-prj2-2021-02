package com.g02.btfdao.dao;

/**
 *  Classes implementing this Interface need to provide a No-Args Constructor (can be private)
 *  If you need to initialize something that would normally be done in the proper Constructor, you can
 *  declare a method with the signature "private void afterConstruction()" that will be called after all fields (that are saved in the db) are filled
 *  If you need to do something (like save an unsupported List in an Array) before the object is deconstructed, then add a method with the signature "private void beforeDeconstruction()"
 */
public interface Savable {
//    default void afterConstruction() {
//    }
}
