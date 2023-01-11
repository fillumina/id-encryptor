package com.fillumina.keyencryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CachedLongEncryptorTest extends AbstractLongEncryptorTest {

    @Override
    LongEncryptor createEncryptor(String key) {
        return new CachedLongEncryptor(key);
    }

}
