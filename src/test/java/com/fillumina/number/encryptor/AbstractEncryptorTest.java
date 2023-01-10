package com.fillumina.number.encryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractEncryptorTest {

    abstract LongEncryptor createEncryptor(String key);

    @Test
    public void shouldConvert0() {
        assertConvert(createEncryptor("xyzwkj"), 0);
    }

    @Test
    public void shouldSignMatter() {
        LongEncryptor ne = createEncryptor("barabba");
        long enc = ne.encrypt(14);
        assertEquals(-1311372286519607063L, enc);
        long dec = ne.decrypt(1311372286519607063L);
        assertNotEquals(14, dec);
    }

    @Test
    public void shouldConvertTheNumberBack() {
        LongEncryptor ne = createEncryptor("1234567");

        for (long l=0; l<1_000; l++) {
            assertConvert(ne, l);
            assertConvert(ne, l * 1123121L);
            assertConvert(ne, l * 239283791123121L);
        }
    }

    @Test
    public void shouldPasswordBePaddedOrTruncated() {
        assertConvert(createEncryptor("ambarabaciccicocco"), 0L, 1L, 12L, 2387987L);
        assertConvert(createEncryptor("1234567"), 0L, 1L, 12L, 2387987L);
        assertConvert(createEncryptor("xyz"), 0L, 1L, 12L, 2387987L);
        assertConvert(createEncryptor("0"), 0L, 1L, 12L, 2387987L);
    }

    @Test
    public void shouldDifferentPasswordGetDifferentEncoding() {
        long enc1 = createEncryptor("0").encrypt(12345L);
        long enc2 = createEncryptor("0123").encrypt(12345L);
        assertNotEquals(enc1, enc2);
    }

    static void assertConvert(LongEncryptor ne, long... array) {
        for (long l : array) {
            long enc = ne.encrypt(l);
            long dec = ne.decrypt(enc);
            assertEquals(l, dec, "" + l);
        }
    }

}
