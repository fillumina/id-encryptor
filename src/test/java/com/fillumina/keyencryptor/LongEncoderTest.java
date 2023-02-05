package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncoderTest {

    @Test
    public void shouldMask() {
        LongEncoder.mask(10, 123L);
    }

    @Test
    public void shouldNotMaskValueOverflowingBitmaskLimitation() {
        assertThrows(IllegalArgumentException.class,
                () -> LongEncoder.mask(3, 123L));
    }

    @Test
    public void shouldNotMaskValueOverTheLimit() {
        assertThrows(IllegalArgumentException.class,
                () -> LongEncoder.mask(3, 8L));
    }

    @Test
    public void shouldMaskValueLimitMinusOne() {
        LongEncoder.mask(3, 7L);
    }

    @Test
    public void shouldThrowExceptionIfMaskBitIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> LongEncoder.mask(-2, 2));
    }

    @Test
    public void shouldNotAcceptMask0() {
        assertThrows(IllegalArgumentException.class,
                () -> LongEncoder.mask(0, 2));
    }

    @Test
    public void shouldextractNodeIdFromEncodedValueFor9BitsNode() {
        for (int i=0; i<511; i++) {
            LongEncoder enc = new LongEncoder(9, i);
            long encoded = enc.encode(1234L);
            assertEquals(i, enc.extractNodeIdFromEncodedValue(encoded));
        }
    }

    @Test
    public void shouldextractNodeIdFromEncodedValueFor8BitsNode() {
        for (int i=0; i<255; i++) {
            LongEncoder enc = new LongEncoder(8, i);
            long encoded = enc.encode(1234L);
            assertEquals(i, enc.extractNodeIdFromEncodedValue(encoded));
        }
    }

    @Test
    public void shouldextractNodeIdFromEncodedValueFor7BitsNode() {
        for (int i=0; i<127; i++) {
            LongEncoder enc = new LongEncoder(7, i);
            long encoded = enc.encode(1234L);
            assertEquals(i, enc.extractNodeIdFromEncodedValue(encoded));
        }
    }

    @Test
    public void shouldDecodeBigValue() {
        LongEncoder enc = new LongEncoder(12, 12);
        long l = 225179981368L;
        assertTrue(l < enc.maxValue());
        long encoded = enc.encode(l);
        assertNotEquals(l, encoded);
        assertEquals(l, enc.decode(encoded));
    }

    @Test
    public void shouldDecode() {
        LongEncoder enc = new LongEncoder(12, 12);
        final long maxValue = enc.maxValue();
        for (long l=0; l<maxValue; l+= maxValue / 10_000) {
            long encoded = enc.encode(l);
            assertNotEquals(l, encoded);
            assertEquals(l, enc.decode(encoded));
        }
    }

    @Test
    public void shouldHaveDifferentMaxValues() {
        LongEncoder enc = new LongEncoder(12, 12, false);
        LongEncoder encJs = new LongEncoder(12, 12, true);

        assertTrue(encJs.maxValue() < enc.maxValue());
    }

    @Test
    public void shouldDecodeWithJavascriptCompliance() {
        LongEncoder enc = new LongEncoder(12, 77, true);
        final long maxValue = enc.maxValue();
        final long maxJsValue = 1L << 52;
        for (long l=0; l<maxValue; l+= maxValue / 10_000) {
            long encoded = enc.encode(l);
            assertNotEquals(l, encoded);
            assertEquals(l, enc.decode(encoded));
            assertTrue(encoded < maxJsValue);
        }
    }

}
