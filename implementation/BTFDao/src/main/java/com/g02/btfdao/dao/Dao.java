package com.g02.btfdao.dao;

import com.g02.btfdao.utils.Savable;

import java.util.Collection;
import java.util.function.Predicate;

public class Dao<E extends Savable> {
    @SafeVarargs
    final E[] insert(E... e) {
        return null;
    }

    E[] insert(Collection<E> e) {
        return null;
    }

    @SafeVarargs
    final E[] remove(E... e) {
        return null;
    }

    E[] remove(Collection<E> e) {
        return null;
    }

    E[] remove(Predicate<E> predicate) {
        return null;
    }

    E update(E e) {
        return null;
    }

    E[] get(Predicate<E> predicate) {
        return null;
    }

    E getFirst(Predicate<E> predicate) {
        return null;
    }

    E[] getAll() {
        return get(x -> true);
    }

}
