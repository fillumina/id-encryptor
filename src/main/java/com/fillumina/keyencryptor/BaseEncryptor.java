package com.fillumina.keyencryptor;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Uses
 * <a href='https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm/'>ECB/NoPadding</a>
 * on the given algorithm to encrypt padded byte arrays. Using ECB while generally discouraged is
 * ideal if the data to encrypt has as many bits as the encryption block.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class BaseEncryptor {

    private final int paddingBytes;
    private final Cipher encryptionCipher;
    private final Cipher decryptionCipher;

    /**
     * @param algorithm a Java
     * <a href='https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html'>Algorithm
     * Standard Name</a>
     * @param pad how many bytes the input/output shoudl be padded with (depends on how many bits
     * the algorithm block econdes)
     * @param key a string representing a key (password)
     */
    public BaseEncryptor(String algorithm, int pad, String key) {
        this.paddingBytes = pad;
        String cipherAlgorithm = algorithm + "/ECB/NoPadding";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    processPassword(paddingBytes, key), algorithm);
            encryptionCipher = Cipher.getInstance(cipherAlgorithm);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            decryptionCipher = Cipher.getInstance(cipherAlgorithm);
            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] data) {
        final byte[] padded = pad(paddingBytes, data);
        try {
            synchronized (encryptionCipher) {
                return encryptionCipher.doFinal(padded);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] data) {
        final byte[] padded = pad(paddingBytes, data);
        try {
            synchronized (decryptionCipher) {
                return decryptionCipher.doFinal(padded);
            }
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
        final int length = data.length;
        if (length < bytes) {
            byte[] padded = new byte[bytes];
            System.arraycopy(data, 0, padded, 0, length);
            return padded;
        }
        if (length > bytes) {
            byte[] trunc = new byte[bytes];
            System.arraycopy(data, 0, trunc, 0, bytes);
            return trunc;
        }
        return data;
    }

    static byte[] processPassword(int padding, String password) {
        try {
            byte[] result = new byte[padding];
            byte[] array = password.getBytes("UTF-8");
            for (int i=0; i<array.length; i++) {
                result[i % padding] ^= array[i];
            }
            return result;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
