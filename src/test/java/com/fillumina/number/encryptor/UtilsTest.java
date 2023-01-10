package com.fillumina.number.encryptor;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UtilsTest {

    @Test
    public void testConvertLongToByteArray() {
        Stream.of(0L, 1L, 2L, 12L, 1000L, 1234567890L)
                .forEach(l -> assertConversion(l));

    }

    private void assertConversion(long l) {
        byte[] array = Utils.convertLongToByteArray(l);
        long converted = Utils.convertByteArrayToLong(array);
        assertEquals(l, converted);
    }

    @Test
    public void shouldPadAndTruncate() {
        for (int i=0; i<16; i++) {
            assertEquals(8, Utils.padTo8Bytes(new byte[i]).length);
        }
    }

}
