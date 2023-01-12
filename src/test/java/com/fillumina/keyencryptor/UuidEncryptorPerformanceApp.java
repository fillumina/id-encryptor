package com.fillumina.keyencryptor;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorPerformanceApp {

    public static void main(String[] args) {
        test("simple", 10_000, 1, new UuidEncryptor("1234"));
        test("cached", 10_000, 1, new CachedUuidEncryptor("1234"));

        test("simple-repeated", 10, 1000, new UuidEncryptor("1234"));
        test("cached-repeated", 10, 1000, new CachedUuidEncryptor("1234"));
    }

    private static boolean test(String title, final int iterations, int subIterations, final UuidEncryptor ne) {
        UUID[] array = new UUID[subIterations];
        for (int k=0; k<subIterations; k++) {
            array[k] = UUID.randomUUID();
        }
        long start = System.currentTimeMillis();
        for (int i=0; i<iterations; i++) {
            for (int k=0; k<subIterations; k++) {
                if (ne.encrypt(array[k]) == null) {
                    // fight code eviction
                    throw new AssertionError("never happen");
                }
            }
        }
        long elapsedMs = System.currentTimeMillis() - start;
        final int totalIterations = iterations * subIterations;
        System.out.println(title + " encrypted " + totalIterations +
                " values in " + elapsedMs + " ms, " +
                (totalIterations * 1.0 / elapsedMs) + " encryptions per ms");
        return false;
    }
}
