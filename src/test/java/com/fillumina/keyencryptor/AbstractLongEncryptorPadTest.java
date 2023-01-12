package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AbstractLongEncryptorPadTest {

    @Test
    public void shouldPadAndTruncate8Bytes() {
        for (int i=0; i<16; i++) {
            assertEquals(8, BaseEncryptor.pad(8, new byte[i]).length);
        }
    }

    @Test
    public void shouldPadAndTruncate16Bytes() {
        for (int i=0; i<32; i++) {
            assertEquals(16, BaseEncryptor.pad(16, new byte[i]).length);
        }
    }

    @Test
    public void shouldNotPadIfPaddingIsSetTo0() {
        for (int i=0; i<32; i++) {
            assertEquals(i, BaseEncryptor.pad(0, new byte[i]).length);
        }
    }
}
