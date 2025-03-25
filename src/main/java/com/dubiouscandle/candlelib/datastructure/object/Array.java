package com.dubiouscandle.candlelib.datastructure.object;

/**
 * No checks are performed for bounds or null values in this class. It is the
 * responsibility of the user to ensure: - Array indices are within bounds (0 <=
 * index < size). - Null values are handled appropriately if necessary. Any
 * out-of-bounds access or invalid operations will not result in runtime
 * exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class Array<T> {
	public T[] items;
	public int size;

	@SuppressWarnings("unchecked")
	public Array() {
		items = (T[]) new Object[16];
		size = 0;
	}

	/**
	 * removes and returns the last element in this array
	 * 
	 * @return the last element in this array
	 */
	public T poll() {
		assert size > 0;

		T poll = items[--size];
		items[size] = null;
		return poll;
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
	 * removes the element at the specified index by overwriting it with the last
	 * element in the array
	 *
	 * @param index the index of the element to remove
	 */
	public void remove(int index) {
		size--;
		items[index] = items[size];
		items[size] = null;
	}

	/**
	 * removes the element at the specified index by shifting all other elements
	 * down
	 *
	 * @param index the index of the element to remove
	 */
	public void removeOrdered(int index) {
		System.arraycopy(items, index + 1, items, index, items.length - index - 1);
	}

	/**
	 * clears this array of all values
	 */
	public void clear() {
		for (int i = 0; i < size; i++) {
			items[i] = null;
		}
		size = 0;
	}

	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(items[0]);
		for (int i = 1; i < size; i++) {
			sb.append(',').append(' ').append(items[i]);
		}
		sb.append(']');
		return sb.toString();
	}
}
