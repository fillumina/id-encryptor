package com.fillumina.keyencryptor;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolder {

    private static Encryptor<Long> longEncryptor;
    private static Encryptor<UUID> uuidEncryptor;

    public static Encryptor<Long> getLongEncryptor() {
        return longEncryptor;
    }

    public static Encryptor<UUID> getUuidEncryptor() {
        return uuidEncryptor;
    }

    public static void initEncryptorsWithPassword(String password) {
        longEncryptor = new CachedEncryptor<>(new LongEncryptor(password));
        uuidEncryptor = new CachedEncryptor<>(new UuidEncryptor(password));
    }
}
