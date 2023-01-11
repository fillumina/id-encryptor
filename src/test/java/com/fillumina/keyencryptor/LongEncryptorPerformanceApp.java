package com.fillumina.keyencryptor;

import com.fillumina.keyencryptor.LongEncryptor;
import com.fillumina.keyencryptor.CachedLongEncryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptorPerformanceApp {

    public static void main(String[] args) {
        test("simple", 10_000, 1, new LongEncryptor("1234"));
        test("cached", 10_000, 1, new CachedLongEncryptor("1234"));

        test("simple-repeated", 10, 1000, new LongEncryptor("1234"));
        test("cached-repeated", 10, 1000, new CachedLongEncryptor("1234"));
    }

    private static boolean test(String title, final int iterations, int subIterations, final LongEncryptor ne) {
        long start = System.currentTimeMillis();
        long accumulator = 0; // just to avoid code eviction
        for (long l=0; l<iterations; l++) {
            for (long k=0; k<subIterations; k++) {
                accumulator += ne.encrypt(l);
            }
        }
        long elapsedMs = System.currentTimeMillis() - start;
        if (accumulator == 0) {
            return true;
        }
        final int totalIterations = iterations * subIterations;
        System.out.println(title + " encrypted " + totalIterations +
                " values in " + elapsedMs + " ms, " +
                (totalIterations * 1.0 / elapsedMs) + " encryptions per ms");
        return false;
    }
}
