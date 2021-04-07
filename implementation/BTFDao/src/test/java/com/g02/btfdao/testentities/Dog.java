package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.Ignore;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.utils.Savable;

import javax.xml.catalog.Catalog;

public class Dog implements Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public String name;
    @Ignore
    @ForeignKey("cat.id")
    public int cat;

    public Dog(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dog() {
    }
}
