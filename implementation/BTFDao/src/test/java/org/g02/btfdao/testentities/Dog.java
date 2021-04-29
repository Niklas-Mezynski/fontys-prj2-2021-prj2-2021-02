package org.g02.btfdao.testentities;

import org.g02.btfdao.annotations.FieldName;
import org.g02.btfdao.annotations.Ignore;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;

import java.util.StringJoiner;

@TableName("Hunde")
public class Dog implements Savable {
    @PrimaryKey
    @FieldName("HundeName")
    private String name;
    public Cat buddy;
    @Ignore
    private String shouldbeignored;
    @FieldName("Hundealter")
    public int age;
    private Cat secretbuddy;

    public Dog(String name, Cat buddy, int age, Cat secretbuddy) {
        this.name = name;
        this.buddy = buddy;
        this.age = age;
        this.secretbuddy = secretbuddy;
        shouldbeignored = "Alarm";
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dog.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("buddy=" + buddy)
                .add("shouldbeignored='" + shouldbeignored + "'")
                .add("age=" + age)
                .add("secretbuddy=" + secretbuddy)
                .toString();
    }

    private Dog() {
    }
}
