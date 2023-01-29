package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        String encryptedString = key;
        if (LongCrockfordConverter.isValidCharArray(encryptedString.toCharArray())) {
            Long encryptedLong = LongCrockfordConverter.fromString(encryptedString);
            Long value = EncryptorsHolder.getLongEncryptor().decrypt(encryptedLong);
            return value;
        } else {
            UUID encryptedUuid = UUID.fromString(encryptedString);
            UUID decryptedUuid = EncryptorsHolder.getUuidEncryptor().decrypt(encryptedUuid);
            return decryptedUuid;
        }
    }

}
