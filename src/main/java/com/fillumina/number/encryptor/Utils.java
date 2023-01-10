package com.fillumina.number.encryptor;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class Utils {

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

    public static byte[] pad(byte[] data) {
        if (data == null) {
            return new byte[8];
        }
        if (data.length < 8) {
            byte[] padded = new byte[8];
            System.arraycopy(data, 0, padded, 0, data.length);
            return padded;
        }
        if (data.length > 8) {
            byte[] trunc = new byte[8];
            System.arraycopy(data, 0, trunc, 0, 8);
            return trunc;
        }
        return data;
    }

}
