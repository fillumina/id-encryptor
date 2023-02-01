package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Cache<K, V> {

    public static final Cache<?,?> NULL = new Cache<Object, Object>() {
        @Override
        public Object get(Object key) {
            return null;
        }

        @Override
        public Object put(Object key, Object value) {
            return null;
        }
    };

    public static <K,V> Cache<K,V> getNoCache() {
        return (Cache<K, V>) NULL;
    }

    V get(K key);

    V put(K key, V value);

}
