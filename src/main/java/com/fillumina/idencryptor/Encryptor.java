package com.fillumina.idencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Encryptor<T> {

    public static final Encryptor<Object> NULL = new Encryptor<Object>() {
        @Override public Object decrypt(Object t) { return t; }
        @Override public Object encrypt(Object t) { return t; }
    };

    @SuppressWarnings("unchecked")
    public static <T> Encryptor<T> getPassThrough() {
        return (Encryptor<T>) NULL;
    }

    T decrypt(T t);

    T encrypt(T t);
}
