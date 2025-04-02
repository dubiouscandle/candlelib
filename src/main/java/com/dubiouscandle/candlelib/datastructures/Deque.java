package com.dubiouscandle.candlelib.datastructures;

/**
 * A deque implementation using an array. No checks are performed for bounds or
 * null values in this class. It is the responsibility of the user to ensure: -
 * Array indices are within bounds (0 <= index < size). - Null values are
 * handled appropriately if necessary. Any out-of-bounds access or invalid
 * operations will not result in runtime exceptions (e.g.,
 * ArrayIndexOutOfBoundsException).
 * 
 * @param <T>
 */
public class Deque<T> {
	public T[] buffer;
	/**
	 * the head of this deque, inclusive
	 */
	public int head;
	/**
	 * the tail of this deque, exclusive
	 */
	public int tail;

	@SuppressWarnings("unchecked")
	public Deque() {
		buffer = (T[]) new Object[16];
		head = 0;
		tail = 0;
	}

	/**
	 * @return the element at the head of this deque
	 */
	public T peek() {
		return buffer[head];
	}

	/**
	 * removes and returns the element at the head of this deque
	 * 
	 * @return the element that was at the head of this deque
	 */
	public T poll() {
		final T poll = buffer[head];
		buffer[head] = null;
		head = (head + 1) & (buffer.length - 1);
		return poll;
	}

	/**
	 * pushes the specified element to the head of this deque
	 * 
	 * @param e
	 */
	public void push(final T e) {
		if (head == tail + 1) {
			resize();
		}

		head = (head - 1) & (buffer.length - 1);
		buffer[head] = e;
	}

	@SuppressWarnings("unchecked")
	private void resize() {
		T[] resized = (T[]) new Object[buffer.length << 1];
		int s = size();

		if (head < tail) {
			System.arraycopy(buffer, head, resized, 0, buffer.length - 1);
		} else {
			System.arraycopy(buffer, head, resized, 0, buffer.length - head);
			System.arraycopy(buffer, 0, resized, buffer.length - head, tail);
		}

		head = 0;
		tail = s;
		buffer = resized;
	}

	/**
	 * @return the number of elements in this deque
	 */
	public int size() {
		return (tail - head) & (buffer.length - 1);
	}

	/**
	 * appends the specified element to the tail of this deque
	 * 
	 * @param e
	 */
	public void append(final T e) {
		if (head == tail + 1) {
			resize();
		}

		buffer[tail] = e;
		tail = (tail + 1) & (buffer.length - 1);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append('[');

		int i = head;
		sb.append(buffer[i]);
		i = (i + 1) & (buffer.length - 1);
		while (i != tail) {
			sb.append(',').append(' ').append(buffer[i]);
			i = (i + 1) & (buffer.length - 1);
		}
		sb.append(']');

		return sb.toString();
	}

	/**
	 * @param index
	 * @return the element at the specified index
	 */
	public T get(int index) {
		return buffer[(head + index) & (buffer.length - 1)];
	}
}
