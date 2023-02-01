package com.fillumina.keyencryptor;

import java.security.SecureRandom;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolder {

    private static class Holder {
        private final Encryptor<Long> longEncryptor;
        private final Encryptor<UUID> uuidEncryptor;
        private final Cache<Object,Object> cache;
        private final long uuidMostSignificantLong;

        private Holder(Encryptor<Long> longEncryptor, Encryptor<UUID> uuidEncryptor,
                Cache<Object, Object> cache, long uuidMostSignificantLong) {
            this.longEncryptor = longEncryptor;
            this.uuidEncryptor = uuidEncryptor;
            this.cache = cache;
            this.uuidMostSignificantLong = uuidMostSignificantLong;
        }

        public String encryptLong(Long value) {
            return (String) cache.getOrCreate(value, () -> {
                Long encryptedLong = longEncryptor.encrypt(value);
                return LongCrockfordConverter.toString(encryptedLong);
            });
        }

        public Long decryptLong(String value) {
            return (Long) cache.getOrCreate(value, () -> {
                Long encryptedLong = LongCrockfordConverter.fromString(value);
                return longEncryptor.decrypt(encryptedLong);
            });
        }

        public String encryptUuid(UUID value) {
            return (String) cache.getOrCreate(value, () -> {
                UUID encryptedUuid = uuidEncryptor.encrypt(value);
                return encryptedUuid.toString();
            });
        }

        public UUID decryptUuid(String value) {
            return (UUID) cache.getOrCreate(value, () -> {
                UUID encryptedUuid = UUID.fromString(value);
                return uuidEncryptor.decrypt(encryptedUuid);
            });
        }

        public String encryptLongAsUuid(long fieldId, Long id) {
            if (id == null) {
                return null;
            }
            UUID uuid = new UUID(uuidMostSignificantLong | fieldId, id);
            return encryptUuid(uuid);
        }

        public Long decryptLongAsUuid(String value) {
            UUID uuid = decryptUuid(value);
            return uuid.getLeastSignificantBits();
        }

    }

    private static Holder holder;

    public static String encryptLong(Long value) {
        return holder.encryptLong(value);
    }

    public static Long decryptLong(String value) {
        return holder.decryptLong(value);
    }

    public static String encryptUuid(UUID value) {
        return holder.encryptUuid(value);
    }

    public static UUID decryptUuid(String value) {
        return holder.decryptUuid(value);
    }

    public static String encryptLongAsUuid(long fieldId, Long id) {
        return holder.encryptLongAsUuid(fieldId, id);
    }

    public static Long decryptLongAsUuid(String value) {
        return holder.decryptLongAsUuid(value);
    }

    public static boolean isInitialized() {
        return holder != null;
    }

    public static void initEncryptorsWithPassword(String password) {
        initEncryptorsWithPasswordAndNodeId(password, new SecureRandom().nextInt());
    }

    public static void initEncryptorsWithPasswordAndNodeId(String password, int nodeId) {
        builder()
            .longEncryptor(new LongEncryptor(password))
            .uuidEncryptor(new UuidEncryptor(password))
            .cache(new ConcurrentCache<Object,Object>())
            .uuidMostSignificantLong(((long)nodeId) << 32)
            .build();
    }

    /** No encryptions will be performed. */
    public static void initEncryptorsAsPassThrough() {
        builder().build();
    }

    public static class Builder {

        private Encryptor<Long> longEncryptor = Encryptor.getPassThrough();
        private Encryptor<UUID> uuidEncryptor = Encryptor.getPassThrough();
        private Cache<Object,Object> cache = Cache.getNoCache();
        private long uuidMostSignificantLong = 0;

        public Builder longEncryptor(final Encryptor<Long> value) {
            this.longEncryptor = value;
            return this;
        }

        public Builder uuidEncryptor(final Encryptor<UUID> value) {
            this.uuidEncryptor = value;
            return this;
        }

        public Builder cache(final Cache<Object,Object> value) {
            this.cache = value;
            return this;
        }

        public Builder uuidMostSignificantLong(final long value) {
            this.uuidMostSignificantLong = value;
            return this;
        }

        /** @return true if no holder was previously created. */
        public void build() {
            holder = new Holder(longEncryptor, uuidEncryptor, cache, uuidMostSignificantLong);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
