package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;

@TableName("cats")
public class Cat implements Savable {

    @PrimaryKey(autogen = true)
    public int id;

    public String name;

    public Cat(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Cat() {
    }
}
