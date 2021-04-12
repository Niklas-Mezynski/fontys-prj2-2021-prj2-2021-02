package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Savable;

import java.util.Arrays;
import java.util.StringJoiner;

@TableName("dogs")
public class Dog implements Savable {

    @PrimaryKey(autogen = true)
    @NotNull
    public int id;
//    @PrimaryKey
    @FieldName("test")
    public String name;
    @ForeignKey("com.g02.btfdao.testentities.Cat#id")
    public int[] cat;

    public Dog(int id, String name) {
        this.id = id;
        this.name = name;
//        this.cat=cat;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dog.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("cat=" + Arrays.toString(cat))
                .toString();
    }
}
