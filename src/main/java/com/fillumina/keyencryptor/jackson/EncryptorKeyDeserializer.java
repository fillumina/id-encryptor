package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeyDeserializer extends KeyDeserializer implements ContextualKeyDeserializer {

    private long seed;

    public EncryptorKeyDeserializer(long seed) {
        this();
        this.seed = seed;
    }

    public EncryptorKeyDeserializer() {
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        String encryptedString = key;
        if (LongCrockfordConverter.isValidCharArray(encryptedString.toCharArray())) {
            return EncryptorsHolder.decryptLong(seed, encryptedString);
        } else {
            return EncryptorsHolder.decryptUuid(encryptedString);
        }
    }

    @Override
    public KeyDeserializer createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        EncryptableKey ann = property.getAnnotation(EncryptableKey.class);
        if (ann != null) {
            return new EncryptorKeyDeserializer(ann.value());
        }
        return null;
    }

}
