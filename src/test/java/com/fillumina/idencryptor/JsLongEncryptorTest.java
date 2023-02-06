package com.fillumina.idencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JsLongEncryptorTest {
    private static final String KEY = "abracadabra";
    private static final String TWEAK = "mysto";

    public static void main(String[] args) {
        JsLongEncryptor enc = new JsLongEncryptor(KEY, TWEAK);
        for (long l=0; l<1_00; l++) {
            long encrypted = enc.encrypt(l);
             System.out.println("" + l + " = \t" + encrypted);
        }
    }

    @Test
    public void testDecrypt() {
        JsLongEncryptor enc = new JsLongEncryptor(KEY, TWEAK);
        assertEncrypting(enc, 1234567L);
    }

    @Test
    public void shouldCheckExtendedRange() {
        JsLongEncryptor enc = new JsLongEncryptor(KEY, TWEAK);
        final long max = (1L << 50);
        for (long l=0; l<max; l+=max/1_000) {
            assertEncrypting(enc, l);
        }
    }

    @Test
    public void shouldCheckRange() {
        JsLongEncryptor enc = new JsLongEncryptor(KEY, TWEAK);
        for (long l=0; l<1_00; l++) {
            assertEncrypting(enc, l);
        }
    }

    private void assertEncrypting(JsLongEncryptor enc, final long value) {
        long encrypted = enc.encrypt(value);
        assertNotEquals(value, encrypted);
        assertTrue(encrypted < (1L << 52), "" + encrypted);
        long decrypted = enc.decrypt(encrypted);
        assertEquals(value, decrypted);
        // System.out.println("" + value + " = \t" + encrypted);
    }

}
