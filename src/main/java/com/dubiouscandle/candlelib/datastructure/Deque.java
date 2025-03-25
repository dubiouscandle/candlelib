package com.dubiouscandle.candlelib.datastructure;

public class Deque<T> {
	public T[] items;
	public int pointer = 0;
	public int size;

	@SuppressWarnings("unchecked")
	public Deque() {
		items = (T[]) new Object[16];
	}

	public T pop() {
		return items[(pointer + --size) & (items.length - 1)];
	}

	public void addLast(T e) {
		items[(pointer + size++) & (items.length - 1)] = e;
	}

	public T poll() {
		size--;
		T ret = items[pointer];
		pointer = (pointer + 1) & (items.length - 1);
		return ret;
	}

	public void addFirst(T e) {
		size++;
		pointer = (pointer - 1) & (items.length - 1);
		items[pointer] = e;
	}

	public T first() {
		return items[pointer];
	}

	public T last() {
		return items[pointer + size - 1];
	}

	public T get(int index) {
		return items[(pointer + index) & (items.length - 1)];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append('[');

		for (int i = 0; i < size; i++) {
			if (i > 0) {
				sb.append(',').append(' ');
			}
			sb.append(get(i));
		}

		sb.append(']');

		return sb.toString();
	}

	public static void main(String[] args) {
		Deque<Character> deque = new Deque<>();

		deque.addFirst('1');
		deque.addFirst('2');
		deque.addFirst('3');
		deque.addLast('4');
		deque.addLast('5');
		deque.addLast('6');
		System.out.println(deque);
		deque.poll();
		System.out.println(deque);
		deque.pop();
		System.out.println(deque);
		for (char i = 'A'; i <= 'Z'; i++) {
			deque.addFirst(i);
		}
		System.out.println(deque);
	}
}
