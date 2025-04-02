package com.dubiouscandle.candlelib.datastructures;

/**
 * An Object to Object map implemented using open addressing and double hashing.
 * This class does not guarantee especially fast putting and contains checking
 * after many removals. Therefore it is necessary for the user to manually call
 * {@link #rehash()} when necessary to maintain performance.
 */
public class ObjObjMap<K, V> {
	public static final Object TOMBSTONE = new Object();
	@SuppressWarnings("unchecked")
	private final V tombstone = (V) TOMBSTONE;
	/**
	 * the keys of this map for convenient iteration. do not modify this array!
	 */
	public K[] keys;

	/**
	 * the values of this map for convenient iteration. do not modify this array!
	 */
	public V[] values;

	/**
	 * the number of key-value pairs in this map. do not modify this field!
	 */
	public int size;

	@SuppressWarnings("unchecked")
	public ObjObjMap() {
		size = 0;
		keys = (K[]) new Object[16];
		values = (V[]) new Object[16];
	}

	/**
	 * @param key
	 * @return the value associated with the specified key, or null if there is none
	 */
	public V get(K key) {
		int mask = values.length - 1;
		int i = key.hashCode();

		if (values[i & mask] == null) {
			return null;
		}

		int d = doubleHash(i);
		i &= mask;

		int end = i;

		do {
			if (keys[i].equals(key) && values[i] != tombstone && values[i] != null) {
				return values[i];
			}

			i = (i + d) & mask;
		} while (values[i] != null && i != end);

		return null;
	}

	/**
	 * removes the specified key and its associated value from this map
	 * 
	 * @param key
	 * @return the value that was associated with the specified key, or
	 *         {@link #defaultValue} if there was none
	 */
	public V remove(K key) {
		int mask = values.length - 1;
		int i = key.hashCode();

		if (values[i & mask] == null) {
			return null;
		}

		int d = doubleHash(i);
		i &= mask;

		int end = i;

		do {
			if (keys[i].equals(key) && values[i] != tombstone && values[i] != null) {
				V oldValue = values[i];
				values[i] = tombstone;
				size--;
				return oldValue;
			}

			i = (i + d) & mask;
		} while (values[i] != null && i != end);

		return null;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the overwritten value, or {@link #defaultValue} if there was none
	 */
	public V put(K key, V value) {
		int mask = values.length - 1;
		int i = key.hashCode();

		if (values[i & mask] != null) {
			int d = doubleHash(i);
			i &= mask;

			int firstTombstone = -1;
			int end = i;
			do {
				if (keys[i].equals(key) && values[i] != TOMBSTONE && values[i] != null) {
					V oldValue = values[i];
					values[i] = value;
					return oldValue;
				} else if (firstTombstone == -1 && values[i] == TOMBSTONE) {
					firstTombstone = i;
				}
				i = (i + d) & mask;
			} while (values[i] != null && i != end);

			if (firstTombstone != -1) {
				i = firstTombstone;
			}
		}

		i &= mask;
		keys[i] = key;
		values[i] = value;
		size++;
		if (size << 1 > values.length) {
			resize(values.length << 1);
		}
		return null;
	}

	public boolean containsKey(K key) {
		int mask = values.length - 1;
		int i = key.hashCode();

		if (values[i & mask] == null) {
			return false;
		}

		int d = doubleHash(i);
		i &= mask;

		int end = i;

		do {
			if (keys[i].equals(key) && values[i] != tombstone && values[i] != null) {
				return true;
			}

			i = (i + d) & mask;
		} while (values[i] != null && i != end);

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
		for (int i = 0; i < values.length; i++) {
			values[i] = null;
		}
	}

	/**
	 * resized and rehashes all keys and values in this array
	 * 
	 * @param newSize the new size of the underlying map. this value should be a
	 *                power of 2
	 */
	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		K[] oldKeys = keys;
		V[] oldValues = values;

		values = (V[]) new Object[newSize];
		keys = (K[]) new Object[newSize];

		for (int i = 0; i < oldKeys.length; i++) {
			if (oldValues[i] == null || oldValues[i] == TOMBSTONE) {
				continue;
			}

			V value = oldValues[i];
			K key = oldKeys[i];

			int mask = values.length - 1;
			int j = key.hashCode();
			int d = doubleHash(j);
			j &= mask;

			while (values[j] != null) {
				j = (j + d) & mask;
			}

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
			if (values[i] != TOMBSTONE && values[i] != null) {
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
