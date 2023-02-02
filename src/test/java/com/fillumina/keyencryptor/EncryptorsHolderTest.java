package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolderTest {

    @Test
    public void shouldReturnTrueIfInitialized() {

        EncryptorsHolder.builder().build();

        assertTrue(EncryptorsHolder.isInitialized());
    }

    @Test
    public void shouldXorGetBackTheRightValue() {
        LongEncryptor enc = new LongEncryptor("abracadabra");
        long a = 123L;
        long b = 456L;

        long encrypted = enc.encrypt(a ^ b);
        long decrypted = enc.decrypt(encrypted);

        assertEquals(a, decrypted ^ b);
        assertEquals(b, decrypted ^ a);
    }
}
