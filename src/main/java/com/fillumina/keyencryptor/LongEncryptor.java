package com.fillumina.keyencryptor;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * Blowfish works on blocks of 64 bits.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptor extends BaseEncryptor {
    private static final String ALGORITHM = "Blowfish";

    public LongEncryptor(String key) {
        super(ALGORITHM, 8, key);
    }

    public long encrypt(long l) {
        byte[] data = convertLongToByteArray(l);
        byte[] enc = encrypt(data);
        return convertByteArrayToLong(enc);
    }

    public long decrypt(long l) {
        byte[] data = convertLongToByteArray(l);
        byte[] plain = decrypt(data);
        return convertByteArrayToLong(plain);
    }

    public static byte[] convertLongToByteArray(long l) {
        if (l == 0) {
            return new byte[8];
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        longBuffer.put(l);
        return byteBuffer.array();
    }

    public static long convertByteArrayToLong(byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        return longBuffer.get();
    }

}
