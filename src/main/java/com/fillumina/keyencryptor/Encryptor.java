package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface Encryptor<T> {

    T decrypt(T l);

    T encrypt(T l);
}
