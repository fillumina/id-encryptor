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

        public Long decryptLong(String value) {
            Long result = (Long) cache.get(value);
            if (result == null) {
                Long encryptedLong = LongCrockfordConverter.fromString(value);
                result = longEncryptor.decrypt(encryptedLong);
                cache.put(value, result);
            }
            return result;
        }

        public String encryptUuid(UUID value) {
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

        public UUID decryptUuid(String value) {
            UUID result = (UUID) cache.get(value);
            if (result == null) {
                UUID encryptedUuid = UUID.fromString(value);
                result = uuidEncryptor.decrypt(encryptedUuid);
                cache.put(value, result);
            }
            return result;
        }

        public String encryptLongAsUUID(long fieldValue, Long id) {
            if (id == null) {
                return null;
            }
            UUID uuid = new UUID(uuidMostSignificantLong | fieldValue, id);
            return encryptUuid(uuid);
        }

        public Long decryptLongAsUUID(String value) {
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

    public static String encryptLongAsUuid(long fieldValue, Long id) {
        return holder.encryptLongAsUUID(fieldValue, id);
    }

    public static Long decryptLongAsUUID(String value) {
        return holder.decryptLongAsUUID(value);
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

        public void build() {
            holder = new Holder(longEncryptor, uuidEncryptor, cache, uuidMostSignificantLong);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
