package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorTest extends AbstractUuidEncryptorTest {

    public UuidEncryptor createEncryptor(String key) {
        return new UuidEncryptor(key);
    }

}
