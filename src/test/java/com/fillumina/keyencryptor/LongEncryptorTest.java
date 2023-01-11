package com.fillumina.keyencryptor;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptorTest extends AbstractLongEncryptorTest {

    public static void main(String[] args) {
        LongEncryptor ne = new LongEncryptor("barabba");

        for (long l=0; l<100; l++) {
            assertConvert(ne, l);
            System.out.println("" + l + "\t" + ne.encrypt(l));
        }
    }

    @Override
    LongEncryptor createEncryptor(String key) {
        return new LongEncryptor(key);
    }

    @Test
    public void testConvertLongToByteArray() {
        Stream.of(0L, 1L, 2L, 12L, 1000L, 1234567890L)
                .forEach(l -> assertConversion(l));

    }

    private void assertConversion(long l) {
        byte[] array = LongEncryptor.convertLongToByteArray(l);
        long converted = LongEncryptor.convertByteArrayToLong(array);
        assertEquals(l, converted);
    }

}
