package com.dubiouscandle.candlelib.algorithm;

import java.util.Random;

public class ParallelArrays {
	private static final Random RANDOM = new Random();

	/**
	 * sorts both the items array and values array in ascending order according to
	 * the values array from indices min to max (both inclusive)
	 * 
	 * @param <T>
	 * @param items
	 * @param values
	 * @param min    the minimum index (inclusive)
	 * @param max    the maximum index (inclusive)
	 */
	public static <T> void quicksort(T[] items, int[] values, int min, int max) {
		if (max - min <= 16) {
			insertionSort(items, values, min, max);
			return;
		}

		int p1 = values[min];
		int p2 = values[max];

		if (p1 > p2) {
			int temp = p1;
			p1 = p2;
			p2 = temp;

			T tempItem = items[min];
			items[min] = items[max];
			items[max] = tempItem;
		}

		int i = min + 1;
		int j = max - 1;
		int k = min + 1;

		while (k <= j) {
			if (values[k] < p1) {
				swap(items, values, i, k);

				i++;
			} else if (values[k] > p2) {
				swap(items, values, j, k);

				j--;
				continue;
			}
			k++;
		}

		swap(items, values, min, i - 1);
		swap(items, values, max, j + 1);

		quicksort(items, values, min, i - 1);
		quicksort(items, values, i, j);
		quicksort(items, values, j + 1, max);
	}

	private static <T> void insertionSort(T[] items, int[] values, int min, int max) {
		for (int i = min + 1; i <= max; i++) {
			int key = values[i];
			T keyItem = items[i];
			int j = i - 1;

			while (j >= min && values[j] > key) {
				values[j + 1] = values[j];
				items[j + 1] = items[j];
				j = j - 1;
			}
			values[j + 1] = key;
			items[j + 1] = keyItem;
		}
	}

	private static <T> void swap(T[] items, int[] values, int a, int b) {
		T tempItem = items[a];
		items[a] = items[b];
		items[b] = tempItem;

		int tempValue = values[a];
		values[a] = values[b];
		values[b] = tempValue;
	}

	public static <T> void shuffle(T[] items, int[] values, int min, int max, Random random) {
		for (int i = max - 1; i > min; i--) {
			int j = random.nextInt(i + 1);

			T temp = items[i];
			items[i] = items[j];
			items[j] = temp;

			int tempVal = values[i];
			values[i] = values[j];
			values[j] = tempVal;
		}
	}

	public static <T> void shuffle(T[] items, int[] values, int min, int max) {
		for (int i = max - 1; i > min; i--) {
			int j = RANDOM.nextInt(i + 1);

			T temp = items[i];
			items[i] = items[j];
			items[j] = temp;

			int tempVal = values[i];
			values[i] = values[j];
			values[j] = tempVal;
		}
	}

}
