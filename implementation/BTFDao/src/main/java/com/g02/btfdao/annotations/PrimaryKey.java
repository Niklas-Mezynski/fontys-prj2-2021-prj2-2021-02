package com.g02.btfdao.annotations;

public @interface PrimaryKey {
    String name() default "";
    boolean autogen() default false;
}
