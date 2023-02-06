package com.fillumina.idencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongCrockfordConverterTest {

    public static void main(String[] args) {
        System.out.println(LongCrockfordConverter.toString(123L));
    }

    @Test
    public void testToString() {
        String s = LongCrockfordConverter.toString(123L);
        assertEquals(13, s.length());
    }

    @Test
    public void testFrom() {
        assertConvert(0);
        assertConvert(123L);
        assertConvert(456L);
        assertConvert(789L);
        assertConvert(-1);
        assertConvert(Long.MIN_VALUE);
        assertConvert(Long.MAX_VALUE);
    }

    private void assertConvert(long value) {
        String s = LongCrockfordConverter.toString(value);
        assertEquals(13, s.length());
        long converted = LongCrockfordConverter.fromString(s);
        assertEquals(value, converted);
    }
}
