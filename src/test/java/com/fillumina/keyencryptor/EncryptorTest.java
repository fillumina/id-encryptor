package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorTest {

    @Test
    public void testGetPassThrough() {
        Encryptor<Integer> encryptor = Encryptor.getPassThrough();

        for (int i=-100; i<100; i++) {
            assertEquals(i, encryptor.encrypt(i));
            assertEquals(i, encryptor.decrypt(i));
        }
    }
}
