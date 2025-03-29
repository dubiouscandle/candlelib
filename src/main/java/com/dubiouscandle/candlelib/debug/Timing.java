package com.dubiouscandle.candlelib.debug;

import java.util.Random;

import com.dubiouscandle.candlelib.datastructure.object.Set;

public class Timing {
	public static void main(String[] args) {
		Runnable run = new Runnable() {

			@Override
			public void run() {
				Set<Integer> set = new Set<>();
				Random random = new Random(1001);
				int m = 100000;
				for (int i = 0; i < 50_000_000; i++) {
					int ch = random.nextInt();
					if (ch == 0) {
						set.remove(random.nextInt(m));
					} else if (ch == 1) {
						set.add(random.nextInt(m));
					} else if (ch == 2) {
						if (set.contains(random.nextInt(m))) {
							set.remove(random.nextInt(m));
						}
					}
				}
				System.out.println(set.toString().substring(0, 1));
				System.out.println(set.contains(random.nextInt()));
			}

		};

		run.run();
		run.run();
		run.run();

		Debug.timeAvg(run, 10);
	}
	//0.3959779208
	//0.3962020541
	
	//0.3949509541
	//0.396733
}
