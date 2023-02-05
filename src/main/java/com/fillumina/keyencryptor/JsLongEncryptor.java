package com.fillumina.keyencryptor;

import com.privacylogistics.FF3Cipher;
import java.nio.charset.StandardCharsets;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Encodes long values limited to 52 bits (limit for Javascript integers)
 * into 52 bits encrypted values.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JsLongEncryptor implements Encryptor<Long> {

    /**
     * see https://en.wikipedia.org/wiki/Format-preserving_encryption
     */
    private final FF3Cipher ff3;

    public JsLongEncryptor(String key, String tweak) {
        ff3 = new FF3Cipher(createHexKeyFromString(16, key),
                createHexKeyFromString(8, tweak),
                "0123456789abcdef");
    }

    private String createHexKeyFromString(int targetSizeInBytes, String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] array = new byte[targetSizeInBytes];
        for (int i=0; i<targetSizeInBytes; i++) {
            array[i] = (byte) (array[i] ^ keyBytes[i % keyBytes.length]);
        }
        return convertToHexString(array);
    }

    private String convertToHexString(byte[] array) {
        StringBuilder buf = new StringBuilder();
        for (int i=0; i<array.length; i++) {
            buf.append(String.format("%02x", array[i]));
        }
        return buf.toString();
    }

    @Override
    public Long decrypt(Long t) {
        try {
            String ciphertext = String.format("%013x", t);
            String decrypted = ff3.decrypt(ciphertext);
             return Long.parseLong(decrypted, 16);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Long encrypt(Long t) {
        try {
            String ciphertext = String.format("%013x", t);
            String encrypted = ff3.encrypt(ciphertext);
             return Long.parseLong(encrypted, 16);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new RuntimeException(ex);
        }
    }

}
