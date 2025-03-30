package com.dubiouscandle.candlelib.datastructure.primitive;

/**
 * No checks are performed for bounds or null values in this class. It is the
 * responsibility of the user to ensure: - Array indices are within bounds (0 <=
 * index < size). - Null values are handled appropriately if necessary. Any
 * out-of-bounds access or invalid operations will not result in runtime
 * exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class IntVector {
	public int[] items;
	public int size;

	public IntVector() {
		items = new int[16];
		size = 0;
	}

	/**
	 * removes and returns the last element in this vector
	 * 
	 * @return the last element in this vector
	 */
	public int poll() {
		return items[--size];
	}

	/**
	 * adds the specified element to this vector
	 *
	 * @param e the element to add
	 */
	public void add(int value) {
		if (size == items.length) {
			int[] resized = new int[size * 2];
			System.arraycopy(items, 0, resized, 0, items.length);

			items = resized;
		}

		items[size++] = value;
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
	 * element in the array
	 *
	 * @param index the index of the element to remove
	 */
	public void remove(int index) {
		items[index] = items[--size];
	}

	/**
	 * sets the size of this array to 0
	 */
	public void clear() {
		size = 0;
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
