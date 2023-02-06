package com.fillumina.idencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class GenericEncryptor<T> extends BaseEncryptor implements Encryptor<T> {

    public GenericEncryptor(String algorithm, int pad, String key) {
        super(algorithm, pad, key);
    }

    @Override
    public T encrypt(T l) {
        byte[] data = convertToByteArray(l);
        byte[] enc = encrypt(data);
        return convertFromByteArray(enc);
    }

    @Override
    public T decrypt(T l) {
        byte[] data = convertToByteArray(l);
        byte[] plain = decrypt(data);
        return convertFromByteArray(plain);
    }

    protected abstract byte[] convertToByteArray(T t);
    protected abstract T convertFromByteArray(byte[] array);
}
