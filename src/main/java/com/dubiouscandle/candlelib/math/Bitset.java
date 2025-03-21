package com.dubiouscandle.candlelib.math;

public class Bitset {
    public int size;
    private int[] bits;

    public Bitset(int size) {
        this.size = size;
        bits = new int[(size + 31) / 32];
    }

    public void add() {

    }

}
