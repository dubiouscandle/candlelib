package com.dubiouscandle.candlelib.datastructure.object;

import java.util.Random;

import com.dubiouscandle.candlelib.debug.Debug;

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

	public T peek() {
		return buffer[head];
	}

	public T poll() {
		final T poll = buffer[head];
		buffer[head] = null;
		head = (head + 1) & (buffer.length - 1);
		return poll;
	}

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

	public int size() {
		return (tail - head) & (buffer.length - 1);
	}

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

//		System.out.println(sb);
		return sb.toString();
	}

	public T get(int index) {
		return buffer[(head + index) & (buffer.length - 1)];
	}

	public static void main(String[] args) {
		int n = 1_000_000;
		Debug.timeAvg(() -> {
			java.util.ArrayDeque<Integer> d1 = new java.util.ArrayDeque<>();
			Random random1 = new Random(1234);
			StringBuilder out1 = new StringBuilder();
			for (int i = 0; i < n; i++) {
				int ch = random1.nextInt(5);
				if (ch == 0) {
					out1.append(d1.pollFirst());
				} else if (ch == 1) {
					out1.append(d1.peekFirst());
				} else if (ch == 2) {
					d1.addFirst(random1.nextInt());
				} else if (ch == 3) {
					d1.addLast(random1.nextInt());
				} else if (ch == 4) {
//					out1.append(d1);
				}
			}
		}, 100);

		Debug.timeAvg(() -> {
			Deque<Integer> d1 = new Deque<>();
			Random random1 = new Random(1234);
			StringBuilder out1 = new StringBuilder();
			for (int i = 0; i < n; i++) {
				int ch = random1.nextInt(5);
				if (ch == 0) {
					out1.append(d1.poll());
				} else if (ch == 1) {
					out1.append(d1.peek());
				} else if (ch == 2) {
					d1.push(random1.nextInt());
				} else if (ch == 3) {
					d1.append(random1.nextInt());
				} else if (ch == 4) {
//					out1.append(d1);
				}

			}
		}, 100);
	}
}
