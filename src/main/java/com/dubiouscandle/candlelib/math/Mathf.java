package com.dubiouscandle.candlelib.math;

public class Mathf {
    public static final float PI = (float) Math.PI;
    public static final float TAU = 2f * PI;
    public static final float HALF_PI = 0.5f * PI;

    public static final float E = (float) Math.E;

    private Mathf() {
    }

    public static float sqrt(float n) {
        float x = n;
        float next;

        do {
            x = next = (x + n / x) * 0.5f;
        } while (x != next);

        return x;
    }


}
