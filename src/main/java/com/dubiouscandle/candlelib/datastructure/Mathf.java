package com.dubiouscandle.candlelib.datastructure;

public class FloatMath {
    private FloatMath() {
    }

    public static float sqrt(float n) {
        float x = n;
        float next;

        do {
            x = next = (x + n / x) * 0.5f;
        } while (x != next);

        return x;
    }

    {

    }
}
