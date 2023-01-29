package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import java.io.IOException;
import java.util.UUID;

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
