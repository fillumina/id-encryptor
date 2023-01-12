package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CachedUuidEncryptorTest extends AbstractUuidEncryptorTest {

    public UuidEncryptor createEncryptor(String key) {
        return new CachedUuidEncryptor("dAtAbAsE98765432");
    }

}
