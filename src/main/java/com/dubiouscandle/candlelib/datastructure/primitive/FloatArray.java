package com.dubiouscandle.candlelib.datastructure.primitive;

/**
 * No checks are performed for bounds or null values in this class. It is the
 * responsibility of the user to ensure: - Array indices are within bounds (0 <=
 * index < size). - Null values are handled appropriately if necessary. Any
 * out-of-bounds access or invalid operations will not result in runtime
 * exceptions (e.g., ArrayIndexOutOfBoundsException).
 */
public class FloatArray {
	public float[] items;
	public int size;

	public FloatArray() {
		items = new float[16];
		size = 0;
	}

	/**
	 * removes and returns the last element in this array
	 * 
	 * @return the last element in this array
	 */
	public float poll() {
		return items[--size];
	}

	/**
	 * adds the specified element to this array
	 *
	 * @param e the element to add
	 */
	public void add(int value) {
		if (size == items.length) {
			float[] resized = new float[size * 2];
			System.arraycopy(items, 0, resized, 0, items.length);

			items = resized;
		}

		items[size++] = value;
	}

	/**
	 * @param index the index of the element to get
	 * @return the element at the specified index
	 */
	public float get(int index) {
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
	 * removes the element at the specified index by shifting all other elements
	 * down
	 *
	 * @param index the index of the element to remove
	 */
	public void removeOrdered(int index) {
		System.arraycopy(items, index + 1, items, index, items.length - index - 1);
	}

}
