package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongAsUuidEncryptorKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        return EncryptorsHolder.decryptLongAsUUID(key);
    }

}
