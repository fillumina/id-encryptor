package com.fillumina.idencryptor;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * Blowfish works on blocks of 64 bits.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptor extends GenericEncryptor<Long> {

    public LongEncryptor(String key) {
        super("Blowfish", 8, key);
    }

    @Override
    protected byte[] convertToByteArray(Long l) {
        if (l == 0) {
            return new byte[8];
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        longBuffer.put(l);
        return byteBuffer.array();
    }

    @Override
    protected Long convertFromByteArray(byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        return longBuffer.get();
    }

}
