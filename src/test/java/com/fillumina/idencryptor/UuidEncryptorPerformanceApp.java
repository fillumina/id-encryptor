package com.fillumina.idencryptor;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorPerformanceApp extends AbstractPerformanceApp<UUID> {

    public static void main(String[] args) {
        UuidEncryptorPerformanceApp app = new UuidEncryptorPerformanceApp();
        final UuidEncryptor uuidEncryptor = new UuidEncryptor("1234");
        final CachedEncryptor<UUID> cachedEncryptor = new CachedEncryptor<>(uuidEncryptor);

        app.test("simple", 10_000, 1, uuidEncryptor);
        app.test("cached", 10_000, 1, cachedEncryptor);

        app.test("simple-repeated", 10, 1000, uuidEncryptor);
        app.test("cached-repeated", 10, 1000, cachedEncryptor);
    }

    @Override
    UUID next(int k) {
        return UUID.randomUUID();
    }
}
