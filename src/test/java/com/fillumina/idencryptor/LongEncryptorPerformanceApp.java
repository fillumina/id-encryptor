package com.fillumina.idencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptorPerformanceApp extends AbstractPerformanceApp<Long> {

    public static void main(String[] args) {
        LongEncryptorPerformanceApp app = new LongEncryptorPerformanceApp();
        final LongEncryptor longEncryptor = new LongEncryptor("1234");
        final CachedEncryptor<Long> cachedEncryptor = new CachedEncryptor<>(longEncryptor);

        app.test("simple", 10_000, 1, longEncryptor);
        app.test("cached", 10_000, 1, cachedEncryptor);

        app.test("simple-repeated", 10, 1000, longEncryptor);
        app.test("cached-repeated", 10, 1000, cachedEncryptor);
    }

    @Override
    Long next(int k) {
        return (long) k;
    }

}
