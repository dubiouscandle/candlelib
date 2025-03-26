package com.dubiouscandle.candlelib.datastructure.object;

public class Map<K, V> {
	@SuppressWarnings("unchecked")
	private final V tombstone = (V) new Object();

	/**
	 * the keys of this map. do not modify the array!
	 */
	public K[] keys;

	/**
	 * the values of this map. do not modify the array!
	 */
	public V[] values;

	/**
	 * the size of this map. do not modify this value!
	 */
	public int size;
	private int tombstoneCount = 0;

	@SuppressWarnings("unchecked")
	public Map() {
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
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				if (values[i] == tombstone) {

					return null;
				}
				return values[i];
			}
			i = (i + d) & mask;
		}

		return null;
	}

	/**
	 * removes the specified key and its associated value from this map
	 * 
	 * @param key
	 * @return the value that was associated with the specified key, or null if
	 *         there was none
	 */
	public V remove(K key) {
		int mask = values.length - 1;
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				if (values[i] == tombstone) {
					return null;
				}
				V oldValue = values[i];
				values[i] = tombstone;
				size--;
				tombstoneCount++;
				if ((tombstoneCount + size >= values.length * 3 / 4)) {
					rehash();
				}
				return oldValue;
			}
			i = (i + d) & mask;
		}

		return null;
	}

	/**
	 * removes the specified key and value only if the key is currently mapped to
	 * the value
	 * 
	 * @param key
	 * @return if the key and value was found and removed
	 */
	public boolean remove(K key, V value) {
		int mask = values.length - 1;
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				if (values[i].equals(value)) {
					values[i] = tombstone;
					size--;
					tombstoneCount++;
					if ((tombstoneCount + size >= values.length * 3 / 4)) {
						rehash();
					}
					return true;
				} else {
					return false;
				}
			}
			i = (i + d) & mask;
		}

		return false;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the overwritten value, or null if there was none
	 */
	public V put(K key, V value) {
		int mask = values.length - 1;
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				if (values[i] == tombstone) {
					values[i] = value;

					size++;
					if (size << 1 >= values.length) {
						resize(values.length << 1);
					}
					return null;
				} else {
					V oldValue = values[i];
					values[i] = value;
					return oldValue;
				}
			}
			i = (i + d) & mask;
		}

		keys[i] = key;
		values[i] = value;

		size++;
		if (size << 1 >= values.length) {
			resize(values.length << 1);
		}
		return null;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the value already associated with the specified key, or null if the
	 *         value was absent and the specified value is added
	 */
	public V putIfAbsent(K key, V value) {
		int mask = values.length - 1;
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				if (values[i] == tombstone) {
					values[i] = value;
					size++;
					if (size << 1 >= values.length) {
						resize(values.length << 1);
					}
					return null;
				}
				return values[i];
			}
			i = (i + d) & mask;
		}

		keys[i] = key;
		values[i] = value;
		size++;
		if (size << 1 >= values.length) {
			resize(values.length << 1);
		}
		return null;
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
	 * @param key
	 * @return if this set contained the specified key
	 */
	public boolean containsKey(K key) {
		int mask = values.length - 1;
		int hashCode = key.hashCode();
		int i = hashCode & mask;
		int d = doubleHash(hashCode);

		while (values[i] != null) {
			if (keys[i].equals(key)) {
				return values[i] != tombstone;
			}

			i = (i + d) & mask;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		tombstoneCount = 0;

		V[] oldValues = values;
		K[] oldKeys = keys;

		values = (V[]) new Object[values.length];
		keys = (K[]) new Object[keys.length];

		for (int i = 0; i < values.length; i++) {
			if (oldValues[i] != null && oldValues[i] != tombstone) {
				int mask = values.length - 1;
				int hashCode = oldKeys[i].hashCode();
				int j = hashCode & mask;
				int d = doubleHash(hashCode);

				while (values[j] != null) {
					j = (j + d) & mask;
				}

				keys[j] = oldKeys[i];
				values[j] = oldValues[i];
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void resize(int newTableSize) {
		tombstoneCount = 0;
		V[] oldValues = values;
		K[] oldKeys = keys;

		values = (V[]) new Object[newTableSize];
		keys = (K[]) new Object[newTableSize];

		for (int i = 0; i < oldValues.length; i++) {
			if (oldValues[i] != null && oldValues[i] != tombstone) {
				int mask = values.length - 1;
				int hashCode = oldKeys[i].hashCode();
				int j = hashCode & mask;
				int d = doubleHash(hashCode);

				while (values[j] != null) {
					j = (j + d) & mask;
				}

				keys[j] = oldKeys[i];
				values[j] = oldValues[i];
			}
		}
	}

	private int doubleHash(int hashCode) {
		return (hashCode >>> 1 | 0b1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		int c = 0;
		for (int i = 0; c < size; i++) {
			if (values[i] != null && values[i] != tombstone) {
				if (c > 0) {
					sb.append(',').append(' ');
				}
				sb.append(keys[i]).append('=').append(values[i]);
				c++;
			}
		}
		return sb.append(']').toString();
	}

	@Override
	public Map<K, V> clone() {
		Map<K, V> clone = new Map<>();
		clone.values = values.clone();
		clone.keys = keys.clone();
		clone.size = size;
		clone.tombstoneCount = tombstoneCount;

		return clone;
	}
}
