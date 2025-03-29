package com.dubiouscandle.candlelib.debug;

public class Debug {
	private Debug() {
	}

	public static void println(Object... objects) {
		StringBuilder out = new StringBuilder();
		for (Object obj : objects) {
			out.append(obj.toString()).append(',').append(' ');
		}

		System.out.println(out);
	}

	public static void print(double x, double y) {
		System.out.println("(" + x + "," + y + "),");
	}

	public static void time(Runnable run) {
		long timeStart = System.nanoTime();

		run.run();

		double t = (System.nanoTime() - timeStart) / 1_000_000_000.0;
		System.out.println(t);
	}

	public static void timeAvg(Runnable run, int iterations) {
		long timeStart = System.nanoTime();

		for (int i = 0; i < iterations; i++)
			run.run();

		double t = (System.nanoTime() - timeStart) / 1_000_000_000.0 / iterations;
		System.out.println(t);
	}

	public static void timeCompare(Runnable run1, Runnable run2, int iterations) {
		for (int i = 0; i < 10000; i++) {
			run1.run();
			run2.run();
		}

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
}
