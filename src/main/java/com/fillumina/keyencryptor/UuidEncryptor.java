package com.fillumina.keyencryptor;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Uses AES algorithm to encrypt UUIDs. Useful to scramble sequential UUIDs.
 * AES can use blocks of 128 bits (which fits UUID size perfectly) and it's fast.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptor extends BaseEncryptor {
    private static final String ALGORITHM = "AES";

    public UuidEncryptor(String key) {
        super(ALGORITHM, 16, key);
    }

    public UUID encrypt(UUID uuid) {
        byte[] encrypted = encrypt(convertUUIDToBytes(uuid));
        return convertBytesToUUID(encrypted);
    }

    public UUID decrypt(UUID uuid) {
        byte[] decrypted = decrypt(convertUUIDToBytes(uuid));
        return convertBytesToUUID(decrypted);
    }

    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

}
