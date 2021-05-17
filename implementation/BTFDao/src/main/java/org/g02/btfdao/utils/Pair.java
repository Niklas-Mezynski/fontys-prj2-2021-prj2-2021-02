package org.g02.btfdao.utils;

public class Pair<K, V> {

    private final K k;

    private final V v;

    public Pair(K key, V value) {
        this.k = key;
        this.v = value;
    }

    public K key() {
        return k;
    }

    public V value() {
        return v;
    }
}
