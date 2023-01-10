package com.fillumina.number.encryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * The use of Blowfish is required by the fact that it works on block of 64 bits.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptor {
    private static final String BLOWFISH_ECB_NOPAD = "Blowfish/ECB/NoPadding";
    private static final String BLOWFISH = "Blowfish";

    private final SecretKeySpec secretKeySpec;

    public LongEncryptor(String key) {
        try {
            secretKeySpec = new SecretKeySpec(Utils.pad(key.getBytes()), BLOWFISH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long encrypt(long l) {
        byte[] data = Utils.convertLongToByteArray(l);
        byte[] enc = encrypt(secretKeySpec, data);
        return Utils.convertByteArrayToLong(enc);
    }

    public long decrypt(long l) {
        byte[] data = Utils.convertLongToByteArray(l);
        byte[] plain = decrypt(secretKeySpec, data);
        return Utils.convertByteArrayToLong(plain);
    }

    static byte[] encrypt(SecretKeySpec secretKeySpec, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(BLOWFISH_ECB_NOPAD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encoded = cipher.doFinal(Utils.pad(data));
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static byte[] decrypt(SecretKeySpec secretKeySpec, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(BLOWFISH_ECB_NOPAD);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decoded = cipher.doFinal(Utils.pad(data));
            return decoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
