package com.fillumina.number.encryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Uses Blowfish algorithm to encrypt long values. Useful to scramble ordered sequences.
 * The use of Blowfish is required by the fact that it works on block of 64 bits.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaseEncryptor {

    private final String cipherAlgorithm;
    private final SecretKeySpec secretKeySpec;
    private final int paddingBytes;

    public BaseEncryptor(String algorithm, int pad, String key) {
        this.cipherAlgorithm = algorithm + "/ECB/NoPadding";
        this.paddingBytes = pad;
        try {
            secretKeySpec = new SecretKeySpec(
                    pad(paddingBytes, key.getBytes()), algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encoded = cipher.doFinal(pad(paddingBytes, data));
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decoded = cipher.doFinal(pad(paddingBytes, data));
            return decoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] pad(int bytes, byte[] data) {
        if (data == null) {
            return new byte[bytes];
        }
        if (data.length < bytes) {
            byte[] padded = new byte[bytes];
            System.arraycopy(data, 0, padded, 0, data.length);
            return padded;
        }
        if (data.length > bytes) {
            byte[] trunc = new byte[bytes];
            System.arraycopy(data, 0, trunc, 0, bytes);
            return trunc;
        }
        return data;
    }

}
