package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void shouldConvertLong52() {
        final long value = 1234L;
        long encrypted = EncryptorsHolder.encryptEncodedLong52(value);
        long decrypted = EncryptorsHolder.decryptEncodedLong52(encrypted);
        assertEquals(value, decrypted);
    }

    @Test
    public void shouldNotEncryptToLong52AValueBiggerThan52Bits() {
        final long value = (1L << 53) ;
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptorsHolder.encryptEncodedLong52(value);
        });
    }

    @Test
    public void shouldNotDecryptToLong52AValueBiggerThan52Bits() {
        final long value = (1L << 53) ;
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptorsHolder.decryptEncodedLong52(value);
        });
    }

    @Test
    public void shouldEncryptNullValuesIntoNulls() {
        
    }
}
