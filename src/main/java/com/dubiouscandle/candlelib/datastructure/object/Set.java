package com.dubiouscandle.candlelib.datastructure.object;

import java.util.Iterator;

/**
 * open addressing implementation using double hashing. the behavior of null
 * values is undefined.
 * 
 * @param <T> the type of the elements in the set
 */
public class Set<T> implements Iterable<T> {
	@SuppressWarnings("unchecked")
	private T tombstone = (T) Boolean.FALSE;
	private T[] values;
	private int tombstoneCount;
	
	/**
	 * the number of elements in this set. do not change this value!
	 */
	public int size;

	@SuppressWarnings("unchecked")
	public Set() {
		values = (T[]) new Object[16];
		size = 0;
	}

	/**
	 * adds the specified element to this set
	 * 
	 * @param e
	 * @return true if the element was added (the element was not already in the
	 *         set)
	 */
	public boolean add(T e) {
		int mask = values.length - 1;
		int i = e.hashCode() & mask;
		int d = doubleHash(e);

		while (values[i] != null) {
			if (e.equals(values[i])) {
				return false;
			}
			i = (i + d) & mask;
		}

		if (values[i] == tombstone) {
			tombstoneCount--;
		}

		size++;
		values[i] = e;

		if (size << 1 >= values.length) {
			resize(values.length << 1);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		tombstoneCount = 0;

		T[] oldValues = values;
		values = (T[]) new Object[newSize];
		int mask = values.length - 1;

		int count = 0;
		for (int i = 0; count < size; i++) {
			if (oldValues[i] == null || oldValues[i] == tombstone) {
				continue;
			}

			T e = oldValues[i];

			int j = e.hashCode() & mask;
			int d = doubleHash(e);

			while (values[j] != null) {
				j = (j + d) & mask;
			}

			values[j] = e;

			count++;
		}
	}

	/**
	 * rehashes all values
	 */
	@SuppressWarnings("unchecked")
	public void rehash() {
		tombstoneCount = 0;

		T[] oldValues = values;
		values = (T[]) new Object[values.length];

		int mask = values.length - 1;

		int count = 0;
		for (int i = 0; count < size; i++) {
			if (oldValues[i] == null || oldValues[i] == tombstone) {
				continue;
			}

			T e = oldValues[i];

			int j = e.hashCode() & mask;
			int d = doubleHash(e);

			while (values[j] != null) {
				j = (j + d) & mask;
			}

			values[j] = e;

			count++;
		}
	}

	/**
	 * removes the specified element from this set
	 * 
	 * @param e
	 * @return true if the element was removed (the element was in this set before)
	 */
	public boolean remove(T e) {
		int mask = values.length - 1;
		int i = e.hashCode() & mask;
		int d = doubleHash(e);

		while (values[i] != null) {
			if (e.equals(values[i])) {
				values[i] = tombstone;
				size--;
				tombstoneCount++;

				if (tombstoneCount << 2 >= values.length) {
					rehash();
				}
				return true;
			}
			i = (i + d) & mask;
		}

		return false;
	}

	/**
	 * 
	 * @param e
	 * @return if this set contains e
	 */
	public boolean contains(T e) {
		int mask = values.length - 1;
		int i = e.hashCode() & mask;
		int d = doubleHash(e);

		while (values[i] != null) {
			if (e.equals(values[i])) {
				return true;
			}
			i = (i + d) & mask;
		}

		return false;
	}

	private int doubleHash(T e) {
		return (e.hashCode() >>> 1 | 0b1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append('[');
		int i = 0;

		while (values[i] == null || values[i] == tombstone) {
			i++;
		}

		sb.append(values[i]);

		i++;

		for (; i < values.length; i++) {
			if (values[i] == null || values[i] == tombstone) {
				continue;
			}

			sb.append(',').append(' ').append(values[i]);
		}

		sb.append(']');
		return sb.toString();
	}

	/**
	 * returns a new iterator for this set
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int i = 0;
			private int count = 0;

			@Override
			public boolean hasNext() {
				return count < size;
			}

			@Override
			public T next() {
				while (values[i] == null || values[i] == tombstone) {
					i++;
				}
				count++;
				return values[i++];
			}

			@Override
			public void remove() {
				values[i - 1] = tombstone;
				size--;
				count--;
			}
		};
	}
}
