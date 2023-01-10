package com.fillumina.number.encryptor;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * The use of Blowfish is required by the fact that it works on block of 64 bits.
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
