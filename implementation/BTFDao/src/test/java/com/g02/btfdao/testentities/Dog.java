package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Savable;

@TableName("dogs")
public class Dog implements Savable {

    @PrimaryKey(autogen = true)
    @NotNull
    public int id;
    @FieldName("test")
    public String name;
    @ForeignKey("com.g02.btfdao.testentities.Cat#id")
    public int[] cat;

    public Dog(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dog() {
    }
}
