package com.dubiouscandle.candlelib.math;

import java.util.Random;

public class Mathf {
	public static final float PI = (float) Math.PI;
	public static final float TAU = 2f * PI;
	public static final float HALF_PI = 0.5f * PI;
	public static final float QUARTER_PI = 0.25f * PI;

	public static final float E = (float) Math.E;

	private static final int SIN_BITS = 14;
	private static final int SIN_MASK = (1 << SIN_BITS) - 1;
	private static final float SIN_VALUE_TO_INDEX = (1 << SIN_BITS) / TAU;
	private static final float[] SIN_TABLE = new float[1 << SIN_BITS];
	private static final int COS_SHIFT = (1 << SIN_BITS) / 4;

	static {
		for (int i = 0; i < SIN_TABLE.length; i++) {
			SIN_TABLE[i] = (float) Math.sin(i / SIN_VALUE_TO_INDEX);
		}
	}

	/**
	 * @param a
	 * @return the arctangent of a, given that a >= 0
	 */
	public static float atanPos(float a) {
		assert a >= 0;

		float c = (a - 1) / (a + 1);

		float c2 = c * c;
		float c3 = c * c2;
		float c5 = c3 * c2;
		float c7 = c5 * c2;
		float c9 = c7 * c2;
		float c11 = c9 * c2;

		return QUARTER_PI + 0.99997726f * c - 0.33262347f * c3 + 0.19354346f * c5 - 0.11643287f * c7 + 0.05265332f * c9
				- 0.0117212f * c11;
	}

	/**
	 * @param y
	 * @param x
	 * @return the angle theta in radians from the conversion of (x, y) into polar
	 *         coordinates.
	 */
	public static float atan2(float y, float x) {
		if (x == 0) {
			if (y == 0) {
				return 0;
			}
			return y > 0 ? HALF_PI : -HALF_PI;
		}
		if (x > 0) {
			return y >= 0 ? atanPos(y / x) : -atanPos(-y / x);
		}
		if (x < 0) {
			return y >= 0 ? PI - atanPos(y / -x) : -PI + atanPos(-y / -x);
		}

		return 0;
	}

	/**
	 * returns sqrt(x)
	 * 
	 * @param x
	 * @return sqrt(x)
	 */
	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}

	/**
	 * returns 1/sqrt(x)
	 * 
	 * @param x
	 * @return 1/sqrt(x)
	 */
	public static float rsqrt(float x) {
		return 1.0f / (float) Math.sqrt(x);
	}

	/**
	 * uses a lookup table to return the value of sin(x)
	 * 
	 * @param x
	 * @return the cosine of x
	 */
	public static float sin(float x) {
		return SIN_TABLE[(int) (x * SIN_VALUE_TO_INDEX) & SIN_MASK];
	}

	/**
	 * uses a lookup table to return the value of cos(x)
	 * 
	 * @param x
	 * @return the cosine of x
	 */
	public static float cos(float x) {
		return SIN_TABLE[((int) (x * SIN_VALUE_TO_INDEX) + COS_SHIFT) & SIN_MASK];
	}

	public static void main(String[] args) {
		Random random = new Random(31);
		float e = 0;
		for (int i = 0; i < 1_000_000; i++) {
			float x = random.nextFloat(-100, 100);
			float y = random.nextFloat(-100, 100);

			float diff = (float) (Math.atan2(y, x) - atan2(y, x));
			e += diff;
			if (diff > 1E-5)
				System.out.println(diff);
		}
		System.out.println(e);

		System.out.println(atanPos(0));
		for (int i = 0; i < 100; i++) {
			float x = random.nextFloat(5);
			System.out.println("(" + (x) + "," + atanPos(x) + "),");
		}
	}
}
