package com.dubiouscandle.candlelib.datastructures;

/**
 * No checks are performed for bounds or null values in this class. It is the
 * responsibility of the user to ensure: - Array indices are within bounds (0 <=
 * index < size). - Null values are handled appropriately if necessary. Any
 * out-of-bounds access or invalid operations will not result in runtime
 * exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class IntVector {
	/**
	 * the items in this vector
	 */
	public int[] items;
	/**
	 * the size of this vector
	 */
	public int size;

	public IntVector() {
		items = new int[16];
		size = 0;
	}

	/**
	 * adds the specified element to this vector
	 *
	 * @param e the element to add
	 */
	public void add(int value) {
		if (size == items.length) {
			resize(size << 1);
		}

		items[size] = value;
		size++;
	}

	private void resize(int newSize) {
		int[] resized = new int[newSize];
		System.arraycopy(items, 0, resized, 0, items.length);

		items = resized;
	}

	/**
	 * @param index the index of the element to get
	 * @return the element at the specified index
	 */
	public int get(int index) {
		return items[index];
	}

	/**
	 * removes the element at the specified index by overwriting it with the last
	 * element in the vector
	 *
	 * @param index the index of the element to remove
	 */
	public void removeUnordered(int index) {
		size--;
		items[index] = items[size];
	}

	/**
	 * removes the element at the specified index by shifting all other elements
	 * down
	 *
	 * @param index the index of the element to remove
	 */
	public void remove(int index) {
		size--;
		System.arraycopy(items, index + 1, items, index, size - index);
	}

	/**
	 * sets the size of this vector to zero
	 */
	public void clear() {
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
