package org.g02.btfdao.utils;

import java.util.Map;

import static java.util.Map.entry;

public enum TypeMappings {
    ; // look ma, no values.
    public static final Map<Class<?>, String> pgTypeMap = Map.ofEntries(
            entry(String.class, "TEXT"),
            entry(Character.class, "CHAR(1)"),
            entry(Integer.class, "INTEGER"),
            entry(int.class, "INTEGER"),
            entry(Short.class, "SMALLINT"),
            entry(short.class, "SMALLINT"),
            entry(Long.class, "BIGINT"),
            entry(long.class, "BIGINT"),
            entry(java.math.BigDecimal.class, "DECIMAL"),
            entry(java.math.BigInteger.class, "NUMERIC"),
            entry(Float.class, "REAL"),
            entry(float.class, "REAL"),
            entry(Double.class, "DOUBLE PRECISION"),
            entry(double.class, "DOUBLE PRECISION"),
            entry(java.time.LocalDate.class, "DATE"),
            entry(java.time.LocalDateTime.class, "TIMESTAMP"),
            entry(boolean.class, "BOOLEAN"),
            entry(Boolean.class, "BOOLEAN"),
            entry(Integer[].class, "integer[]"), //Experimental
            entry(int[].class, "integer[]"), //Experimental
            entry(Long[].class, "BIGINT[]"),
            entry(long[].class, "BIGINT[]"),
            entry(String[].class, "TEXT[]")
    );

    public static String getTypeName(Class<?> c) {
        return pgTypeMap.get(c);
    }

}