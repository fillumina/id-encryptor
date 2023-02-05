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
        private final LongEncoder longEncoder;
        private final Encryptor<Long> jsLongEncryptor;

        private Holder(Encryptor<Long> longEncryptor, Encryptor<UUID> uuidEncryptor,
                Encryptor<Long> jsLongEncryptor,
                Cache<Object, Object> cache, long uuidMostSignificantLong,
                int nodeBits, long nodeId) {
            this.longEncryptor = longEncryptor;
            this.uuidEncryptor = uuidEncryptor;
            this.cache = cache;
            this.longEncoder = new LongEncoder(nodeBits, nodeId);
            this.uuidMostSignificantLong = uuidMostSignificantLong;
            this.jsLongEncryptor = jsLongEncryptor;
        }

        public Long encryptEncodedLong(long seed, Long value) {
            long encrypted = longEncoder.encode(value);
            return jsLongEncryptor.encrypt(encrypted);
        }

        public Long decryptEncodedLong(long seed, Long value) {
            long decrypted = jsLongEncryptor.decrypt(value);
            return longEncoder.decode(decrypted);
        }

        public String encryptLong(long seed, Long value) {
            long xor = seed ^ value;
            return (String) cache.getOrCreate(xor, () -> {
                Long encryptedLong = longEncryptor.encrypt(xor);
                return LongCrockfordConverter.toString(encryptedLong);
            });
        }

        public Long decryptLong(long seed, String value) {
            return (Long) cache.getOrCreate(value + seed, () -> {
                Long encryptedLong = LongCrockfordConverter.fromString(value);
                return seed ^ longEncryptor.decrypt(encryptedLong);
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

    public static Long encryptEncodedLong(long seed, Long value) {
        return holder.encryptEncodedLong(seed, value);
    }

    public static Long decryptEncodedLong(long seed, Long value) {
        return holder.decryptEncodedLong(seed, value);
    }

    /** Seed is assumed 0. */
    public static String encryptLong(Long value) {
        return holder.encryptLong(0L, value);
    }

    public static String encryptLong(long seed, Long value) {
        return holder.encryptLong(seed, value);
    }

    /** Seed is assumed 0. */
    public static Long decryptLong(String value) {
        return holder.decryptLong(0L, value);
    }

    public static Long decryptLong(long seed, String value) {
        return holder.decryptLong(seed, value);
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
        String pass = password.substring(0, password.length() / 2);
        String tweak = password.substring(password.length() / 2);
        builder()
            .longEncryptor(new LongEncryptor(password))
            .uuidEncryptor(new UuidEncryptor(password))
            .jsLongEncryptor(new JsLongEncryptor(pass, tweak))
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
        private Encryptor<Long> jsLongEncryptor = Encryptor.getPassThrough();
        private Cache<Object,Object> cache = Cache.getNoCache();
        private long uuidMostSignificantLong = 0;
        private int nodeBits;
        private long nodeId;

        public void build() {
            holder = new Holder(longEncryptor, uuidEncryptor, jsLongEncryptor,
                    cache, uuidMostSignificantLong, nodeBits, nodeId);
        }

        public Builder longEncryptor(final Encryptor<Long> value) {
            this.longEncryptor = value;
            return this;
        }

        public Builder uuidEncryptor(final Encryptor<UUID> value) {
            this.uuidEncryptor = value;
            return this;
        }

        public Builder jsLongEncryptor(final Encryptor<Long> value) {
            this.jsLongEncryptor = value;
            return this;
        }

        public Builder cache(final Cache<Object,Object> value) {
            this.cache = value;
            return this;
        }

        /** @return true if no holder was previously created. */
        public Builder uuidMostSignificantLong(final long value) {
            this.uuidMostSignificantLong = value;
            return this;
        }

        public Builder nodeBits(final int value) {
            this.nodeBits = value;
            return this;
        }

        public Builder nodeId(final long value) {
            this.nodeId = value;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
