package com.fillumina.keyencryptor;

import java.security.SecureRandom;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolder {

    private static Encryptor<Long> longEncryptor;
    private static Encryptor<UUID> uuidEncryptor;
    private static long uuidMostSignificantLong;

    private static ConcurrentCache<Object,Object> cache = new ConcurrentCache<Object,Object>();

    @Deprecated
    public static Encryptor<Long> getLongEncryptor() {
        return longEncryptor;
    }

    @Deprecated
    public static Encryptor<UUID> getUuidEncryptor() {
        return uuidEncryptor;
    }

    public static String encryptLong(Long value) {
        if (value == null) {
            return null;
        }
        String result = (String) cache.get(value);
        if (result == null) {
            Long encryptedLong = longEncryptor.encrypt((long)value);
            result = LongCrockfordConverter.toString(encryptedLong);
            cache.put(value, result);
        }
        return result;
    }

    public static Long decryptLong(String value) {
        Long result = (Long) cache.get(value);
        if (result == null) {
            Long encryptedLong = LongCrockfordConverter.fromString(value);
            result = longEncryptor.decrypt(encryptedLong);
            cache.put(value, result);
        }
        return result;
    }

    public static String encryptUuid(UUID value) {
        if (value == null) {
            return null;
        }
        String result = (String) cache.get(value);
        if (result == null) {
            UUID encryptedUuid = uuidEncryptor.encrypt((UUID)value);
            result = encryptedUuid.toString();
            cache.put(value, result);
        }
        return result;
    }

    public static UUID decryptUuid(String value) {
        UUID result = (UUID) cache.get(value);
        if (result == null) {
            UUID encryptedUuid = UUID.fromString(value);
            result = uuidEncryptor.decrypt(encryptedUuid);
            cache.put(value, result);
        }
        return result;
    }

    public static String encryptLongAsUUID(long fieldValue, Long id) {
        if (id == null) {
            return null;
        }
        UUID uuid = new UUID(uuidMostSignificantLong | fieldValue, id);
        return encryptUuid(uuid);
    }

    public static Long decryptLongAsUUID(String value) {
        UUID uuid = decryptUuid(value);
        return uuid.getLeastSignificantBits();
    }

    public static void initEncryptorsWithPassword(String password) {
        initEncryptorsWithPassword(password, new SecureRandom().nextLong());
    }

    public static void initEncryptorsWithPassword(String password, long nodeId) {
        longEncryptor = new LongEncryptor(password);
        uuidEncryptor = new UuidEncryptor(password);
        uuidMostSignificantLong = nodeId << 32;
    }
}
