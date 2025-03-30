package com.dubiouscandle.candlelib.datastructure.object;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class PriorityQueue<T> {
	private T[] heap;
	private int size;
	private Comparator<T> comparator;

	public PriorityQueue() {
		this((Comparator<T>) Comparator.naturalOrder());
	}

	public PriorityQueue(Comparator<T> comparator) {
		this.comparator = comparator;
		this.heap = (T[]) new Object[26];
		this.size = 0;
	}

	public void add(T e) {
		if (size == heap.length) {
			resize(size << 1);
		}

		heap[size] = e;
		heapifyUp(size);
		size++;
	}

	private void resize(int newSize) {
		T[] resized = (T[]) new Object[newSize];
		System.arraycopy(heap, 0, resized, 0, size);
		heap = resized;
	}

	public T poll() {
		if (size == 0)
			return null;

		T result = heap[0];
		heap[0] = heap[size - 1];
		heap[size - 1] = null;
		size--;

		heapifyDown(0);
		return result;
	}

	private void heapifyUp(int index) {
		while (index > 0) {
			int parentIndex = (index - 1) / 2;
			if (compare(heap[index], heap[parentIndex]) >= 0)
				break;
			swap(index, parentIndex);
			index = parentIndex;
		}
	}

	private void heapifyDown(int index) {
		while (true) {
			int leftChild = 2 * index + 1;
			int rightChild = 2 * index + 2;
			int smallest = index;

			if (leftChild < size && compare(heap[leftChild], heap[smallest]) < 0) {
				smallest = leftChild;
			}
			if (rightChild < size && compare(heap[rightChild], heap[smallest]) < 0) {
				smallest = rightChild;
			}
			if (smallest == index)
				break;

			swap(index, smallest);
			index = smallest;
		}
	}

	private int compare(T a, T b) {
		if (comparator != null) {
			return comparator.compare(a, b);
		} else {
			return ((Comparable<T>) a).compareTo(b);
		}
	}

	private void swap(int i, int j) {
		T temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	public T peek() {
		return heap[0];
	}

	@Override
	public String toString() {
		T[] sorted = (T[]) new Object[size];
		System.arraycopy(heap, 0, sorted, 0, size);
		Arrays.sort(sorted);
		return Arrays.toString(sorted);
	}
}
