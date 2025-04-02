package com.dubiouscandle.candlelib.datastructures;

import java.util.Iterator;

/**
 * open addressing implementation using double hashing. the behavior of null
 * values is undefined. this class is not designed for especially fast
 * iteration.
 * 
 * @param <T> the type of the elements in the set
 */
public class UnorderedSet<T> implements Iterable<T> {
	/**
	 * the tombstone marker for this set for convienient iteration
	 */
	public static final Object TOMBSTONE = new Object();

	@SuppressWarnings("unchecked")
	private T tombstone = (T) TOMBSTONE;

	/*
	 * The values of this set for convienient iteration
	 */
	public T[] values;

	/**
	 * the number of elements in this set. do not change this value!
	 */
	public int size;

	@SuppressWarnings("unchecked")
	public UnorderedSet() {
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
		int i = e.hashCode();
		int d = doubleHash(i);
		i &= mask;
		int end = i;

		int firstTombstone = -1;

		if (values[i] != null) {
			do {
				if (e.equals(values[i])) {
					return false;
				} else if (firstTombstone == -1 && values[i] == tombstone) {
					firstTombstone = i;
				}

				i = (i + d) & mask;
			} while (values[i] != null && i != end);
		}

		if (firstTombstone != -1) {
			i = firstTombstone;
		}

		values[i] = e;
		size++;
		if (size << 1 > values.length) {
			resize(values.length << 1);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		T[] oldValues = values;
		values = (T[]) new Object[newSize];
		int mask = values.length - 1;

		for (T e : oldValues) {
			if (e == null || e == tombstone) {
				continue;
			}

			int j = e.hashCode();
			int d = doubleHash(j);
			j &= mask;

			while (values[j] != null) {
				j = (j + d) & mask;
			}

			values[j] = e;
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
		int i = e.hashCode();
		int d = doubleHash(i);
		i &= mask;

		if (values[i] == null) {
			return false;
		}

		int end = i;

		do {
			if (values[i] != tombstone && e.equals(values[i])) {
				size--;
				values[i] = tombstone;
				return true;
			}

			i = (i + d) & mask;
		} while (values[i] != null && i != end);

		return false;
	}

	/**
	 * rehashes all values
	 */
	public void rehash() {
		resize(values.length);
	}

	/**
	 * 
	 * @param e
	 * @return if this set contains e
	 */
	public boolean contains(T e) {
		int mask = values.length - 1;
		int i = e.hashCode();
		int d = doubleHash(i);
		i &= mask;

		if (values[i] == null) {
			return false;
		}

		int end = i;

		do {
			if (e.equals(values[i])) {
				return true;
			}
			i = (i + d) & mask;
		} while (values[i] != null && i != end);

		return false;
	}

	protected int doubleHash(int x) {
		x = (x + 0x7ed55d16) + (x << 12);
		x = (x ^ 0xc761c23c) ^ (x >> 19);
		x = (x + 0x165667b1) + (x << 5);
		x = (x + 0xd3a2646c) ^ (x << 9);
		x = (x + 0xfd7046c5) + (x << 3);
		x = (x ^ 0xb55a4f09) ^ (x >> 16);
		return (x & 1) == 1 ? x : (x << 1 | 1);
	}

	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append('[');
		int i = 0;

		while (values[i] == null || values[i] == tombstone) {
			i++;
		}

		sb.append(values[i++]);

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
