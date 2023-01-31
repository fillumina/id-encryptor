package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import java.io.IOException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorDeserializer extends StdDeserializer<Object> {

    public EncryptorDeserializer() {
        this(null);
    }

    public EncryptorDeserializer(Class<Object> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
        String encryptedString = jp.getText();
        if (LongCrockfordConverter.isValidCharArray(encryptedString.toCharArray())) {
            return EncryptorsHolder.decryptLong(encryptedString);
        } else {
            return EncryptorsHolder.decryptUuid(encryptedString);
        }
    }
}
