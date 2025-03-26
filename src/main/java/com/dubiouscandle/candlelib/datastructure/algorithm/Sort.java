package com.dubiouscandle.candlelib.datastructure.algorithm;

import java.util.Arrays;

public class Sort {
	public static <T> void dualPivotQuicksort(T[] items, int[] values, int low, int high) {
		if (low < high) {
			int[] pivots = partition(items, values, low, high);
			dualPivotQuicksort(items, values, low, pivots[0] - 1);
			dualPivotQuicksort(items, values, pivots[0] + 1, pivots[1] - 1);
			dualPivotQuicksort(items, values, pivots[1] + 1, high);
		}
	}

	private static <T> int[] partition(T[] items, int[] values, int low, int high) {
		if (values[low] > values[high]) {
			swap(items, values, low, high);
		}

		int leftPivot = values[low];
		int rightPivot = values[high];

		int i = low + 1;
		int lt = low + 1;
		int gt = high - 1;

		while (i <= gt) {
			if (values[i] < leftPivot) {
				swap(items, values, i, lt);
				lt++;
			} else if (values[i] > rightPivot) {
				swap(items, values, i, gt);
				gt--;
				i--; // Recheck swapped value
			}
			i++;
		}

		swap(items, values, low, --lt);
		swap(items, values, high, ++gt);

		return new int[] { lt, gt };
	}

	private static <T> void swap(T[] items, int[] values, int i, int j) {
		int tempVal = values[i];
		values[i] = values[j];
		values[j] = tempVal;

		T tempItem = items[i];
		items[i] = items[j];
		items[j] = tempItem;
	}

	public static void main(String[] args) {
		Character[] items = new Character[10];
		int[] values = new int[10];

		for (int i = 0; i < 10; i++) {
			values[i] = i;
			items[i] = (char) ('A' + i);
		}
		
		Sort.dualPivotQuicksort(items, values, 0, values.length - 1);
		System.out.println(Arrays.toString(items));
	}

}
