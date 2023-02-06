package com.fillumina.idencryptor;

import java.util.WeakHashMap;

/**
 * Blowfish encryption is very demanding computationally so a cache might be useful.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CachedEncryptor<T> implements Encryptor<T> {

    private final ConcurrentCache<T, T> encryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    private final ConcurrentCache<T, T> decryptCache =
            new ConcurrentCache(32, WeakHashMap::new);

    private final Encryptor<T> delegate;

    public CachedEncryptor(Encryptor<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T decrypt(T t) {
        T value = decryptCache.get(t);
        if (value != null) {
            return value;
        }
        T decrypted = delegate.decrypt(t);
        decryptCache.put(t, decrypted);
        return decrypted;
    }

    @Override
    public T encrypt(T t) {
        T value = encryptCache.get(t);
        if (value != null) {
            return value;
        }
        T encrypted = delegate.encrypt(t);
        encryptCache.put(t, encrypted);
        return encrypted;
    }

}
