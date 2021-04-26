package testentities;

import com.g02.btfdao.annotations.Ignore;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.dao.Savable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@TableName("Katzen")
public class Cat implements Savable {
    @PrimaryKey(autogen = true)
    private int id;
    @PrimaryKey
    private int secondarypk;
    @Ignore
    private List<Mouse> food;
    private Mouse[] fooddb;

    private Cat() {
    }

    public Cat(int id, List<Mouse> food) {
        this.id = id;
        this.food = new ArrayList<>();
        this.food.addAll(food);
        this.secondarypk = 1312;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cat.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("secondarypk=" + secondarypk)
                .add("food=" + food)
                .toString();
    }

    private void afterConstruction() {
        food = Arrays.stream(fooddb).collect(Collectors.toList());
    }

    private void beforeDeconstruction() {
        fooddb = food.toArray(Mouse[]::new);
    }

    public List<Mouse> getFood() {
        return food;
    }
}
