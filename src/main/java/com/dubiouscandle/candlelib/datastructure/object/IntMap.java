package com.dubiouscandle.candlelib.datastructure.object;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import com.dubiouscandle.candlelib.debug.Debug;

public class IntMap<T> {
	@SuppressWarnings("unchecked")
	private final T tombstone = (T) new Object();

	/**
	 * the keys of this map. do not modify the array!
	 */
	public int[] keys;

	/**
	 * the values of this map. do not modify the array!
	 */
	public T[] values;

	/**
	 * the size of this map. do not modify this field!
	 */
	public int size;
	private int tombstoneCount = 0;

	@SuppressWarnings("unchecked")
	public IntMap() {
		size = 0;
		keys = new int[16];
		values = (T[]) new Object[16];
	}

	/**
	 * @param key
	 * @return the value associated with the specified key, or null if there is none
	 */
	public T get(int key) {
		int mask = values.length - 1;
		int i = key & mask;
		int d = doubleHash(key);

		while (values[i] != null) {
			if (keys[i] == key) {
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
	public T remove(int key) {
		int mask = values.length - 1;
		int i = key & mask;
		int d = doubleHash(key);

		while (values[i] != null) {
			if (keys[i] == key) {
				if (values[i] == tombstone) {
					return null;
				}
				T value = values[i];
				values[i] = tombstone;
				size--;
				tombstoneCount++;
				if ((tombstoneCount + size >= values.length * 3 / 4)) {
					rehash();
				}
				return value;
			}
			i = (i + d) & mask;
		}

		return null;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the overwritten value, or null if there was none
	 */
	public T put(int key, T value) {
		if (size << 1 >= values.length) {
			resize(values.length << 1);
		}

		int mask = values.length - 1;
		int i = key & mask;
		int d = doubleHash(key);

		while (values[i] != null) {
			if (keys[i] == key) {
				if (values[i] == tombstone) {
					values[i] = value;
					size++;
					return null;
				}
				T oldValue = values[i];
				values[i] = value;
				return oldValue;
			}
			i = (i + d) & mask;
		}

		T oldValue = values[i];
		keys[i] = key;
		values[i] = value;

		size++;
		return oldValue;
	}

	/**
	 * puts the key value pair into this map
	 * 
	 * @param key
	 * @param value
	 * @return the value already associated with the specified key, or null if the
	 *         value was absent and the specified value is added
	 */
	public T putIfAbsent(int key, T value) {
		if (size << 1 >= values.length) {
			resize(values.length << 1);
		}

		int mask = values.length - 1;
		int i = key & mask;
		int d = doubleHash(key);

		while (values[i] != null) {
			if (keys[i] == key) {
				if (values[i] == tombstone) {
					values[i] = value;
					size++;
					return null;
				}
				return values[i];
			}
			i = (i + d) & mask;
		}

		keys[i] = key;
		values[i] = value;
		size++;
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
	public boolean containsKey(int key) {
		int mask = values.length - 1;
		int i = key & mask;
		int d = doubleHash(key);

		while (values[i] != null) {
			if (keys[i] == key) {
				return values[i] != tombstone;
			}

			i = (i + d) & mask;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		tombstoneCount = 0;

		T[] oldValues = values;
		int[] oldKeys = keys;

		values = (T[]) new Object[values.length];
		keys = new int[keys.length];

		for (int i = 0; i < values.length; i++) {
			if (oldValues[i] != null && oldValues[i] != tombstone) {
				int mask = values.length - 1;
				int j = oldKeys[i] & mask;
				int d = doubleHash(oldKeys[i]);

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
		T[] oldValues = values;
		int[] oldKeys = keys;

		values = (T[]) new Object[newTableSize];
		keys = new int[newTableSize];

		for (int i = 0; i < oldValues.length; i++) {
			if (oldValues[i] != null && oldValues[i] != tombstone) {
				int mask = values.length - 1;
				int j = oldKeys[i] & mask;
				int d = doubleHash(oldKeys[i]);

				while (values[j] != null) {
					j = (j + d) & mask;
				}

				keys[j] = oldKeys[i];
				values[j] = oldValues[i];
			}
		}
	}

	private int doubleHash(int key) {
		return (key >>> 1 | 0b1);
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
	// 0.463160334
	// 0.460959417
	// 0.44856675

	// 0.440042667
	// 0.426665375
	public static void main(String[] args) {
		Debug.time(() -> {
			Random random = new Random(0);
			HashMap<Integer, Character> map1 = new HashMap<>();
			IntMap<Character> map2 = new IntMap<>();
			String chars = "asdfghjklqwertyuiopzxcvbnm qwertyuiopasdfghjklzxcvbnm meaning";
			for (int i = 0; i < 10000000; i++) {
				int ch = random.nextInt(5);
				char character = chars.charAt(random.nextInt(0, chars.length()));
				int index = random.nextInt(-100, 100);
				Character out1 = null;
				Character out2 = null;

				if (ch == 111) {
					out1 = map1.putIfAbsent(index, character);
					out2 = map2.putIfAbsent(index, character);
				} else if (ch == 111) {
					if (map1.containsKey(index) != map2.containsKey(index)) {
						System.out.println(index);
					}
				} else if (ch == 2) {
					out1 = map1.put(index, character);
					out2 = map2.put(index, character);
				} else if (ch == 1) {
					out1 = map1.get(index);
					out2 = map2.get(index);
				} else {
					out1 = map1.remove(index);
					out2 = map2.remove(index);
				}
				if (!Objects.equals(out1, out2)) {
					System.out.println("ASDJNHAKJSLHDCKNJHANJKLSDCHASDASD");
					System.out.println(out1);
					System.out.println(out2);
					System.out.println(map1);
					System.out.println(map2);
				}
			}
		});
	}
}
