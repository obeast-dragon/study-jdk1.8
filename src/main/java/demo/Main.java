package demo;

import java.util.UUID;

/**
 * @author wxl
 * Date 2022/8/31 10:28
 * @version 1.0
 * Description:
 */
public class Main {

    public static void main(String[] args) {
        SimpleHashMap<Integer, String> map = new SimpleHashMap<>(4);
        map.put(1, "hello1");
        map.put(2, "hello2");
        map.put(3, "hello3");
        map.put(5, "hello2");
        map.put(26723956, "hello");
        map.put(28821108, "dahdaha");
        map.put(28820596, "ccccc");
        map.put(28824692, "vvvvv");

        map.put(29250676, "qqqq");
        map.put(29250740, "vvvzzzvv");
        map.put(29283444, "vvvvmmmv");
        map.put(33488820, "cqcq");

    }

    public static int getIndex(int n, int hash) {
        return (n - 1) & hash;
    }

    static final int hash(Object key) {
        int h;
        //^ 异或
        //>>> 右移
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
