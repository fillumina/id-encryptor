package com.fillumina.keyencryptor;

import com.fillumina.keyencryptor.LongEncryptor;
import com.fillumina.keyencryptor.CachedLongEncryptor;

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
