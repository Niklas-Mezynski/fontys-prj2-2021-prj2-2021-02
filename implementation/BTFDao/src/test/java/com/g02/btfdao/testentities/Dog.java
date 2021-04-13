package com.g02.btfdao.testentities;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Savable;
import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.StringJoiner;

@TableName("dogs")
public class Dog implements Savable {

    @PrimaryKey(autogen = true)
    @NotNull
    public int id;
    @PrimaryKey
    @FieldName("test")
    public String name;
//    @ForeignKey("com.g02.btfdao.testentities.Cat#catid")
//    public int[] cat;
//    @ForeignKey("com.g02.btfdao.testentities.Cat#catid")
//    public int cat2;
    @ForeignKey("com.g02.btfdao.testentities.Cat#catid")
    public Cat[] RealCats;
    @ForeignKey("com.g02.btfdao.testentities.Cat#catid")
    public Cat buddy = new Cat();
//    @ForeignKey("com.g02.btfdao.testentities.Cat#catid")
//    public Cat buddy2 = new Cat();

    public Dog(int id, String name, String breed) {
        this.id = id;
        this.name = name;
//        this.cat=cat;
    }
    private Dog(int id,String name){
        this.id=id;
        this.name=name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dog.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("cat=" + Arrays.toString(RealCats))
                .toString();
    }

}
