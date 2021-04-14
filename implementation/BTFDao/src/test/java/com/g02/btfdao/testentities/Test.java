package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;

@TableName("TestTable")
public class Test implements Savable {
    @PrimaryKey
    public int id;
    @ForeignKey("com.g02.btfdao.testentities.Cat#id")
    public Cat[] ints;
}
