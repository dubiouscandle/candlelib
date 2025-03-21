package com.dubiouscandle.candlelib.datastructure;

public class Array<T> {
    private T[] items;
    private int size;

    @SuppressWarnings("unchecked")
    public Array() {
        items = (T[]) new Object[16];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public void add(T e) {
        if (size == items.length) {
            Object[] resized = new Object[size * 2];
            System.arraycopy(items, 0, resized, 0, items.length);

            items = (T[]) resized;
        }

        items[size++] = e;
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        return items[index];
    }

    public void remove(int index) {
        items[index] = items[size - 1];
    }
}
