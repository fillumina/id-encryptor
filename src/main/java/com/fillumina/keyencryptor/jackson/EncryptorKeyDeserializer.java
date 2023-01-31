package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        String encryptedString = key;
        if (LongCrockfordConverter.isValidCharArray(encryptedString.toCharArray())) {
            return EncryptorsHolder.decryptLong(encryptedString);
        } else {
            return EncryptorsHolder.decryptUuid(encryptedString);
        }
    }

}
