package com.dubiouscandle.candlelib.math;

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

	public static float atanPos(float x) {
		assert x >= 0;

		float c = (x - 1) / (x + 1);

		float c2 = c * c;
		float c3 = c * c2;
		float c5 = c3 * c2;
		float c7 = c5 * c2;
		float c9 = c7 * c2;
		float c11 = c9 * c2;

		return QUARTER_PI + 0.99997726f * c - 0.33262347f * c3 + 0.19354346f * c5 - 0.11643287f * c7 + 0.05265332f * c9
				- 0.0117212f * c11;
	}

	public static float atan2(float y, float x) {
		assert x != 0 || y != 0;

		if (x == 0) {
			return y > 0 ? HALF_PI : -HALF_PI;
		} else if (x > 0) {
			return y > 0 ? atanPos(y / x) : -atanPos(-y / x);
		} else {
			return y > 0 ? atanPos(y / x) + PI : atanPos(y / x) - PI;
		}
	}

	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}

	public static float rsqrt(float x) {
		return 1.0f / (float) Math.sqrt(x);
	}

	public static float sin(float x) {
		return SIN_TABLE[(int) (x * SIN_VALUE_TO_INDEX) & SIN_MASK];
	}

	public static float cos(float x) {
		return SIN_TABLE[((int) (x * SIN_VALUE_TO_INDEX) + COS_SHIFT) & SIN_MASK];
	}
}
