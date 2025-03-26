package com.dubiouscandle.candlelib.debug;

public class Debug {
	private Debug() {
	}

	public static void time(Runnable run) {
		long timeStart = System.nanoTime();

		run.run();

		double t = (System.nanoTime() - timeStart) / 1_000_000_000.0;
		System.out.println(t);
	}

	public static void timeCompare(Runnable run1, Runnable run2, int iterations) {
		long tAccum1 = 0;
		long tAccum2 = 0;

		for (int i = 0; i < iterations; i++) {
			{
				long timeStart = System.nanoTime();

				run1.run();

				tAccum1 += System.nanoTime() - timeStart;
			}
			{
				long timeStart = System.nanoTime();

				run2.run();

				tAccum2 += System.nanoTime() - timeStart;
			}
		}

		System.out.println("1: " + tAccum1 / 1_000_000_000.0);
		System.out.println("2: " + tAccum2 / 1_000_000_000.0);
	}

	public static void warm() {
		for (int i = 0; i < 1_000_000_000; i++) {
			Integer e = Integer.valueOf(i);
			e.byteValue();
		}
	}
}
