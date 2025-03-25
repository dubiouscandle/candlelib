package com.dubiouscandle.candlelib.datastructure;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Set<T> implements Iterable<T> {
	@SuppressWarnings("unchecked")
	private T tombstone = (T) Boolean.FALSE;
	private T[] values;
	public int size;
	private int tombstoneCount;

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

	public static void main(String[] args) {
		Set<Integer> set1 = new Set<Integer>();
		HashSet<Integer> set2 = new HashSet<Integer>();

		Random random = new Random(0);

		for (int i = 0; i < 100000000; i++) {
			int in = random.nextInt(33412);

			int c = random.nextInt(100);
//			System.out.println(in);
			if (in == 1) {
				if (set1.add(c) != set2.add(c)) {
					System.out.println("A");
				}
			} else if (in == 2) {
				if (set1.remove(c) != set2.remove(c)) {
//					System.out.println(set1);
//					System.out.println(set2);
					System.out.println("R");
				}
			} else {
				if (set1.contains(c) != set2.contains(c)) {
					System.out.println("C");
				}
			}
		}

		StringBuilder s1 = new StringBuilder();
		for (Integer c : set1) {
			s1.append(c);
		}
		StringBuilder s2 = new StringBuilder();
		for (Integer c : set2) {
			s2.append(c);
		}
		System.out.println(s1.substring(0, 10));
		System.out.println(s2.substring(0, 10));
		System.out.println(s1.length());
		System.out.println(s2.length());
	}

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
