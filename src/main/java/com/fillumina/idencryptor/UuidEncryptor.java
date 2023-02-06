package com.fillumina.idencryptor;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Uses AES algorithm to encrypt UUIDs. Useful to scramble sequential UUIDs.
 * AES works on blocks of 128 bits.
 * <br>
 * Note that encrypted UUIDs will have their version and variant scrambled with
 * random (totally inaccurate) values but they are still printable and treated as regular
 * UUIDs which is what matters.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptor extends GenericEncryptor<UUID> {

    public UuidEncryptor(String key) {
        super("AES", 16, key);
    }

    @Override
    protected byte[] convertToByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    protected UUID convertFromByteArray(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

}
