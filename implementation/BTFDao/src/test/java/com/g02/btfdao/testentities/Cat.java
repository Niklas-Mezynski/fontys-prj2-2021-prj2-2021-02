package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;

@TableName("cats")
public class Cat {

    @PrimaryKey(autogen = true)
    public int id;

    public String name;

}
