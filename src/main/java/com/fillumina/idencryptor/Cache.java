package com.fillumina.idencryptor;

import java.util.function.Supplier;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Cache<K, V> {

    public static final Cache<?,?> NULL =
            (Object key, Supplier<Object> valueGenerator) -> valueGenerator.get();

    @SuppressWarnings("unchecked")
    public static <K,V> Cache<K,V> getNoCache() {
        return (Cache<K, V>) NULL;
    }

    V getOrCreate(K key, Supplier<V> valueGenerator);

}
