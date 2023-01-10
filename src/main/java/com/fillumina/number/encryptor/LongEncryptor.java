package com.fillumina.number.encryptor;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * The use of Blowfish is required by the fact that it works on block of 64 bits.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptor extends BaseEncryptor {
    private static final String ALGORITHM = "Blowfish";

    public LongEncryptor(String key) {
        super(ALGORITHM, 8, key);
    }

    public long encrypt(long l) {
        byte[] data = Utils.convertLongToByteArray(l);
        byte[] enc = encrypt(data);
        return Utils.convertByteArrayToLong(enc);
    }

    public long decrypt(long l) {
        byte[] data = Utils.convertLongToByteArray(l);
        byte[] plain = decrypt(data);
        return Utils.convertByteArrayToLong(plain);
    }

}
