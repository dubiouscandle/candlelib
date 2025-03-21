package com.dubiouscandle.candlelib.datastructure;

import java.util.ArrayList;

/**
 * No checks are performed for bounds or null values in this class.
 * It is the responsibility of the user to ensure:
 * - Array indices are within bounds (0 <= index < size).
 * - Null values are handled appropriately if necessary.
 * Any out-of-bounds access or invalid operations will result in runtime exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class Array<T> {
    private T[] items;
    public int size;

    @SuppressWarnings("unchecked")
    public Array() {
        new ArrayList<>();
        items = (T[]) new Object[16];
        size = 0;
    }

    /**
     * adds the specified element to this array
     *
     * @param e the element to add
     */
    @SuppressWarnings("unchecked")
    public void add(T e) {
        if (size == items.length) {
            Object[] resized = new Object[size * 2];
            System.arraycopy(items, 0, resized, 0, items.length);

            items = (T[]) resized;
        }

        items[size++] = e;
    }

    /**
     * @param index the index of the element to get
     * @return the element at the specified index
     */
    public T get(int index) {
        return items[index];
    }

    /**
     * removes the element at the specified index by overwriting it with the last element in the array
     *
     * @param index the index of the element to remove
     */
    public void remove(int index) {
        size--;
        items[index] = items[size];
        items[size] = null;
    }

    /**
     * removes the element at the specified index by shifting all other elements down
     *
     * @param index the index of the element to remove
     */
    public void removeOrdered(int index) {
        System.arraycopy(items, index + 1, items, index, items.length - index - 1);
    }
}
