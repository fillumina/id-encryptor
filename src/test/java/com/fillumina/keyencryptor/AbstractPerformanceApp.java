package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractPerformanceApp<T> {

    abstract T next(int k);

    boolean test(String title, final int iterations, int subIterations, final Encryptor<T> ne) {
        T[] array = (T[]) new Object[subIterations];
        for (int k=0; k<subIterations; k++) {
            array[k] = next(k);
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
