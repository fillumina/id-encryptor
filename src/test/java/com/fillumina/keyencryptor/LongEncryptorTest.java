package com.fillumina.keyencryptor;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptorTest {

    public static void main(String[] args) {
        LongEncryptor ne = new LongEncryptor("barabba");

        for (long l=0; l<100; l++) {
            assertConvert(ne, l);
            System.out.println("" + l + "\t" + ne.encrypt(l));
        }
    }

    @Test
    public void shouldConvert0() {
        assertConvert(new LongEncryptor("xyzwkj"), 0);
    }

    @Test
    public void shouldSignMatter() {
        LongEncryptor ne = new LongEncryptor("barabba");
        long enc = ne.encrypt(14L);
        assertEquals(-1311372286519607063L, enc);
        long dec = ne.decrypt(1311372286519607063L);
        assertNotEquals(14, dec);
    }

    @Test
    public void shouldConvertTheNumberBack() {
        LongEncryptor ne = new LongEncryptor("1234567");

        for (long l=0; l<1_000; l++) {
            assertConvert(ne, l);
            assertConvert(ne, l * 1123121L);
            assertConvert(ne, l * 239283791123121L);
        }
    }

    @Test
    public void shouldPasswordBePaddedOrTruncated() {
        assertConvert(new LongEncryptor("ambarabaciccicocco"), 0L, 1L, 12L, 2387987L);
        assertConvert(new LongEncryptor("1234567"), 0L, 1L, 12L, 2387987L);
        assertConvert(new LongEncryptor("xyz"), 0L, 1L, 12L, 2387987L);
        assertConvert(new LongEncryptor("0"), 0L, 1L, 12L, 2387987L);
    }

    @Test
    public void shouldDifferentPasswordGetDifferentEncoding() {
        long enc1 = new LongEncryptor("0").encrypt(12345L);
        long enc2 = new LongEncryptor("0123").encrypt(12345L);
        assertNotEquals(enc1, enc2);
    }

    @Test
    public void testConvertLongToByteArray() {
        Stream.of(0L, 1L, 2L, 12L, 1000L, 1234567890L)
                .forEach(l -> assertConversion(l));

    }

    private void assertConversion(long l) {
        LongEncryptor encryptor = new LongEncryptor("0");
        byte[] array = encryptor.convertToByteArray(l);
        long converted = encryptor.convertFromByteArray(array);
        assertEquals(l, converted);
    }

    static void assertConvert(LongEncryptor ne, long... array) {
        for (long l : array) {
            long enc = ne.encrypt(l);
            long dec = ne.decrypt(enc);
            assertEquals(l, dec, "" + l);
        }
    }

}
