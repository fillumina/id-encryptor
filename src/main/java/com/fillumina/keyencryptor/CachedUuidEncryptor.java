package com.fillumina.keyencryptor;

import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Caches encrypted UUIDs for fast retrieval.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CachedUuidEncryptor extends UuidEncryptor {

    private final ConcurrentCache<UUID, UUID> encryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    private final ConcurrentCache<UUID, UUID> decryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    public CachedUuidEncryptor(String key) {
        super(key);
    }

    @Override
    public UUID decrypt(UUID uuid) {
        UUID value = decryptCache.get(uuid);
        if (value != null) {
            return value;
        }
        value = super.decrypt(uuid);
        decryptCache.put(uuid, value);
        return value;
    }

    @Override
    public UUID encrypt(UUID uuid) {
        UUID value = encryptCache.get(uuid);
        if (value != null) {
            return value;
        }
        value = super.encrypt(uuid);
        encryptCache.put(uuid, value);
        return value;
    }

}
