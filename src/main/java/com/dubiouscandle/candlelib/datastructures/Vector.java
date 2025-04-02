package com.dubiouscandle.candlelib.datastructures;

import java.util.Iterator;

/**
 * No checks are performed for bounds or null values in this class. It is the
 * responsibility of the user to ensure: - Array indices are within bounds (0 <=
 * index < size). - Null values are handled appropriately if necessary. Any
 * out-of-bounds access or invalid operations will not result in runtime
 * exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class Vector<T> implements Iterable<T> {
	/**
	 * the items in this vector
	 */
	public T[] items;
	/**
	 * the size of this vector
	 */
	public int size;

	@SuppressWarnings("unchecked")
	public Vector() {
		items = (T[]) new Object[16];
		size = 0;
	}

	/**
	 * removes and returns the last element in this vector
	 * 
	 * @return the last element in this vector
	 */
	public T pop() {
		assert size > 0;

		size--;
		T poll = items[size];
		items[size] = null;
		return poll;
	}

	/**
	 * adds the specified element to this vector
	 *
	 * @param e the element to add
	 */
	public void add(T e) {
		if (size == items.length) {
			resize(size << 1);
		}

		items[size] = e;
		size++;
	}

	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		Object[] resized = new Object[newSize];
		System.arraycopy(items, 0, resized, 0, items.length);

		items = (T[]) resized;
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
	 * element in the vector
	 *
	 * @param index the index of the element to remove
	 */
	public void removeOrdered(int index) {
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
	public void remove(int index) {
		System.arraycopy(items, index + 1, items, index, items.length - index - 1);
	}

	/**
	 * clears this vector of all values
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

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public T next() {
				return items[i++];
			}

			@Override
			public void remove() {
				items[--i] = items[--size];
				items[size] = null;
			}
		};
	}
}
