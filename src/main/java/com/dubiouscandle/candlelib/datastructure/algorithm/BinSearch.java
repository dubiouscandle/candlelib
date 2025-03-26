package com.dubiouscandle.candlelib.datastructure.algorithm;

import java.util.Comparator;

public class BinSearch {
	/**
	 * searches the indices from min (inclusive) to max (inclusive) for the greatest
	 * element less than or equal to key. if the list is not sorted, the behavior of
	 * this method is undefined.
	 * 
	 * @param arr
	 * @param key
	 * @param min
	 * @param max
	 * @return the index of the greatest element less than or equal to key. this
	 *         method will return min-1 if key is less than all elements and max+1
	 *         if key is greater than all elements
	 */
	public static int floor(int[] arr, int key, int min, int max) {
		while (min <= max) {
			int mid = (min + max) / 2;

			if (arr[mid] <= key) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}

		return max;
	}

	/**
	 * searches the indices from min (inclusive) to max (inclusive) for the smallest
	 * element greater than or equal to key. if the list is not sorted, the behavior
	 * of this method is undefined.
	 * 
	 * @param arr
	 * @param key
	 * @param min
	 * @param max
	 * @return the index of the smallest element greater than or equal to key. this
	 *         method will return min-1 if key is less than all elements and max+1
	 *         if key is greater than all elements
	 */
	public static int ceil(int[] arr, int key, int min, int max) {
		while (min <= max) {
			int mid = (max + min) / 2;

			if (arr[mid] < key) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return min;
	}

	/**
	 * @see #floor(int[] arr, int key, int min, int max)
	 */
	public static int floor(float[] arr, int key, int min, int max) {
		while (min <= max) {
			int mid = (min + max) / 2;

			if (arr[mid] <= key) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}

		return max;
	}

	/**
	 * @see #ceil(int[] arr, int key, int min, int max)
	 */
	public static int ceil(float[] arr, int key, int min, int max) {
		while (min <= max) {
			int mid = (max + min) / 2;

			if (arr[mid] < key) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return min;
	}

	/**
	 * @see #floor(int[] arr, int key, int min, int max)
	 */
	public static <T extends Comparable<T>> int floor(T[] arr, T key, int min, int max) {
		while (min <= max) {
			int mid = (min + max) / 2;

			if (arr[mid].compareTo(key) <= 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}

		return max;
	}

	/**
	 * @see #ceil(int[] arr, int key, int min, int max)
	 */
	public static <T extends Comparable<T>> int ceil(T[] arr, T key, int min, int max) {
		while (min <= max) {
			int mid = (max + min) / 2;

			if (arr[mid].compareTo(key) < 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return min;
	}

	/**
	 * @see #floor(int[] arr, int key, int min, int max)
	 */
	public static <T> int floor(T[] arr, T key, Comparator<T> comparator, int min, int max) {
		while (min <= max) {
			int mid = (min + max) / 2;

			if (comparator.compare(arr[mid], (key)) <= 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}

		return max;
	}

	/**
	 * @see #ceil(int[] arr, int key, int min, int max)
	 */
	public static <T> int ceil(T[] arr, T key, Comparator<T> comparator, int min, int max) {
		while (min <= max) {
			int mid = (max + min) / 2;

			if (comparator.compare(arr[mid], (key)) < 0) {
				min = mid + 1;
			} else {
				max = mid - 1;
			}
		}
		return min;
	}

}
