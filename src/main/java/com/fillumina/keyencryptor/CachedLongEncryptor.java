package com.fillumina.keyencryptor;

import java.util.WeakHashMap;

/**
 * Blowfish encryption is very demanding computationally so a cache might be useful.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CachedLongEncryptor extends LongEncryptor {

    private final ConcurrentCache<Long, Long> encryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    private final ConcurrentCache<Long, Long> decryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    public CachedLongEncryptor(String key) {
        super(key);
    }

    @Override
    public long decrypt(long l) {
        Long value = decryptCache.get(l);
        if (value != null) {
            return value;
        }
        value = super.decrypt(l);
        decryptCache.put(l, value);
        return value;
    }

    @Override
    public long encrypt(long l) {
        Long value = encryptCache.get(l);
        if (value != null) {
            return value;
        }
        value = super.encrypt(l);
        encryptCache.put(l, value);
        return value;
    }

}
