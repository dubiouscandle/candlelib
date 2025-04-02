package com.dubiouscandle.candlelib.datastructures;

/**
 * An int to int map implemented using open addressing and double hashing. This
 * class does not guarantee especially fast putting and contains checking after
 * many removals. Therefore it is necessary for the user to manually call
 * {@link #rehash()} when necessary to maintain performance.
 */
public class IntIntMap {
	public static final int EMPTY = 0;
	public static final int TOMBSTONE = 1;
	public static final int OCCUPIED = 2;

	/**
	 * the default return value in {@link #remove(int)}, {@link #put(int, int)}, and
	 * {@link #get(int)}
	 */
	public int defaultValue;
	/**
	 * the keys of this map for convenient iteration. do not modify this array!
	 */
	public int[] keys;

	/**
	 * the values of this map for convenient iteration. do not modify this array!
	 */
	public int[] values;

	/**
	 * the statuses of this map for convenient iteration. do not modify this array!
	 */
	public int[] status;

	/**
	 * the number of key-value pairs in this map. do not modify this field!
	 */
	public int size;

	public IntIntMap() {
		size = 0;
		keys = new int[16];
		values = new int[16];
		status = new int[16];
	}

	/**
	 * @param key
	 * @return the value associated with the specified key, or {@link #defaultValue}
	 *         if there is none
	 */
	public int get(int key) {
		int mask = values.length - 1;
		int i = key & mask;

		if (status[i] == EMPTY) {
			return defaultValue;
		}

		int d = doubleHash(key);
		int end = i;

		do {
			if (keys[i] == key && status[i] == OCCUPIED) {
				return values[i];
			}

			i = (i + d) & mask;
		} while (status[i] != EMPTY && i != end);

		return defaultValue;
	}

	/**
	 * removes the specified key and its associated value from this map
	 * 
	 * @param key
	 * @return the value that was associated with the specified key, or
	 *         {@link #defaultValue} if there was none
	 */
	public int remove(int key) {
		int mask = values.length - 1;
		int i = key & mask;

		if (status[i] == EMPTY) {
			return defaultValue;
		}

		int d = doubleHash(key);
		int end = i;

		do {
			if (keys[i] == key && status[i] == OCCUPIED) {
				status[i] = TOMBSTONE;
				size--;
				return values[i];
			}

			i = (i + d) & mask;
		} while (status[i] != EMPTY && i != end);

		return defaultValue;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the overwritten value, or {@link #defaultValue} if there was none
	 */
	public int put(int key, int value) {
		int mask = values.length - 1;
		int i = key & mask;

		int firstTombstone = -1;

		if (status[i] != EMPTY) {
			int d = doubleHash(key);
			int end = i;
			do {
				if (keys[i] == key && status[i] == OCCUPIED) {
					int oldValue = values[i];
					values[i] = value;
					status[i] = OCCUPIED;
					if (size << 1 > values.length) {
						resize(values.length << 1);
					}
					return oldValue;
				} else if (firstTombstone == -1 && status[i] == TOMBSTONE) {
					firstTombstone = i;
				}
				i = (i + d) & mask;
			} while (status[i] != EMPTY && i != end);

			if (firstTombstone != -1) {
				i = firstTombstone;
			}
		}

		keys[i] = key;
		values[i] = value;
		status[i] = OCCUPIED;
		size++;
		if (size << 1 > values.length) {
			resize(values.length << 1);
		}
		return defaultValue;
	}

	public boolean containsKey(int key) {
		int mask = values.length - 1;
		int i = key & mask;

		if (status[i] == EMPTY) {
			return false;
		}

		int d = doubleHash(key);
		int end = i;

		do {
			if (keys[i] == key && status[i] == OCCUPIED) {
				return true;
			}

			i = (i + d) & mask;
		} while (status[i] != EMPTY && i != end);

		return false;
	}

	/**
	 * @param x
	 * @return the double hash function for this map
	 */
	protected int doubleHash(int x) {
		x = (x + 0x7ed55d16) + (x << 12);
		x = (x ^ 0xc761c23c) ^ (x >> 19);
		x = (x + 0x165667b1) + (x << 5);
		x = (x + 0xd3a2646c) ^ (x << 9);
		x = (x + 0xfd7046c5) + (x << 3);
		x = (x ^ 0xb55a4f09) ^ (x >> 16);
		return (x & 1) == 1 ? x : (x << 1 | 1);
	}

	/**
	 * clears this set of all keys and values
	 */
	public void clear() {
		size = 0;
		for (int i = 0; i < status.length; i++) {
			status[i] = EMPTY;
		}
	}

	/**
	 * resized and rehashes all keys and values in this array
	 * 
	 * @param newSize the new size of the underlying map. this value should be a
	 *                power of 2
	 */
	private void resize(int newSize) {
		int[] oldKeys = keys;
		int[] oldValues = values;
		int[] oldStatus = status;

		values = new int[newSize];
		keys = new int[newSize];
		status = new int[newSize];

		for (int i = 0; i < oldKeys.length; i++) {
			if (oldStatus[i] != OCCUPIED) {
				continue;
			}

			int value = oldValues[i];
			int key = oldKeys[i];

			int mask = values.length - 1;
			int j = key & mask;
			int d = doubleHash(key);

			while (status[j] != EMPTY) {
				j = (j + d) & mask;
			}

			status[j] = OCCUPIED;
			keys[j] = key;
			values[j] = value;
		}
	}

	/**
	 * rehashes all keys and values in this map
	 */
	public void rehash() {
		resize(values.length);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		boolean first = true;
		for (int i = 0; i < keys.length; i++) {
			if (status[i] == OCCUPIED) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}

				sb.append(keys[i]).append('=').append(values[i]);
			}
		}
		sb.append('}');
		return sb.toString();
	}
}
