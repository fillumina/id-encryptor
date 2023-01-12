package com.fillumina.keyencryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Uses
 * <a href='https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm/'>ECB/NoPadding</a>
 * on the given algorithm to encrypt padded byte arrays. Using ECB while generally discouraged is
 * ideal if the data to encrypt is just as long as the encryption block.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaseEncryptor {

    private final String cipherAlgorithm;
    private final SecretKeySpec secretKeySpec;
    private final int paddingBytes;

    /**
     * @param algorithm a Java
     * <a href='https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html'>Algorithm
     * Standard Name</a>
     * @param pad how many bytes the input/output shoudl be padded with (depends on how many bits
     * the algorithm block econdes)
     * @param key a string representing a key (password)
     */
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
        if (bytes == 0) {
            return data;
        }
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
