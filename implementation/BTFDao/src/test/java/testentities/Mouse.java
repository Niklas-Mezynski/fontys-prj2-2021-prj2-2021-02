package testentities;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.dao.Savable;

import java.util.Arrays;
import java.util.StringJoiner;

@TableName("Ratten")
public class Mouse implements Savable {
    @PrimaryKey
    public long id;
    private int[] arraytest;


    public int[] getArraytest() {
        return arraytest;
    }

    public void setArraytest(int[] arraytest) {
        this.arraytest = arraytest;
    }

    public Mouse(long id) {
        this.id = id;
        this.arraytest = new int[]{0, 1, 3, 45, 78, 61, 1};
    }

    private Mouse() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Mouse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("arraytest=" + Arrays.toString(arraytest))
                .toString();
    }
}
