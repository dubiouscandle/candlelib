package com.dubiouscandle.candlelib.debug;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import com.dubiouscandle.candlelib.datastructures.ObjObjMap;

public class Test1 {
	public static void main(String[] args) {
		HashMap<Integer, Integer> map1 = new HashMap<>();
		ObjObjMap<Integer, Integer> map2 = new ObjObjMap<>();
		Random random = new Random(3);

		for (int i = 0; i < 10_000_000; i++) {
			if (i % 100_000 == 0) {
				System.out.println(i);
				if (map1.toString().length() != map2.toString().length()) {
					System.out.println(map1.toString().substring(0, 50));
					System.out.println(map2.toString().substring(0, 50));
				}
			}
			int ch = random.nextInt(5); // Random operation choice
//			System.out.println(i + " " + ch);
			Integer key = random.nextInt(0, 1001234) * 256;
			Integer value = random.nextInt();

			if (map1.size() != map2.size) {
				System.out.println(map1.size() + " " + map2.size);
			}
			if (ch == 0) { // Test put
				Integer expectedPut = map1.put(key, value);
				Integer actualPut = map2.put(key, value);
				if (!Objects.equals(expectedPut, actualPut)) {
					System.out.println("put mismatch at key: " + key);
					System.out.println(expectedPut);
					System.out.println(actualPut);
				}
			}

			if (ch == 1) { // Test get
				Integer expectedGet = map1.get(key);
				Integer actualGet = map2.get(key);
				if (!Objects.equals(actualGet, expectedGet)) {
					System.out.println("get mismatch at key: " + key);
					System.out.println(expectedGet);
					System.out.println(actualGet);
				}

			}

			if (ch == 2 && random.nextFloat() < 0.1f) { // Test remove
				Integer expectedRemove = map1.remove(key);
				Integer actualRemove = map2.remove(key);
				if (!Objects.equals(actualRemove, expectedRemove)) {
					System.out.println("remove ASDmismatch at key: " + key);
					System.out.println(actualRemove);
					System.out.println(expectedRemove);
				}
			}

			if (ch == 3) { // Test containsKey
				boolean expectedContains = map1.containsKey(key);
				boolean actualContains = map2.containsKey(key);
				if (expectedContains != actualContains) {
					System.out.println("containskey mismatch at key: " + key);
				}
			}

		}

		System.out.println("Test completed!");
	}
}
