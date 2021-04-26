package com.g02.flightsalesfx.helpers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Bundle {

    private Map<String, Object> stringObjectMap = new HashMap<>();

    public <E extends Serializable> void add(String key, E e) {
        stringObjectMap.put(key, e);
    }

    public Object get(String key) {
        return stringObjectMap.getOrDefault(key, null);
    }

    public <E extends Serializable> E get(String key, Class<E> aClass) {
        var o = get(key);
        if (aClass.isInstance(o)) {
            return (E) o;
        }
        return null;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        var o = get(key);
        if (o == null) return defaultValue;
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return defaultValue;
    }

}
