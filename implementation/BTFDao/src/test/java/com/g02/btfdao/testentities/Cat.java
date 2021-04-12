package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;

@TableName("cats")
public class Cat implements Savable {

    @PrimaryKey(autogen = true)
    public int catid;
    @PrimaryKey(autogen = false)
    public String catname;

    public Cat(int id, String name) {
        this.catid = id;
        this.catname = name;
    }

    public Cat() {
    }
}
