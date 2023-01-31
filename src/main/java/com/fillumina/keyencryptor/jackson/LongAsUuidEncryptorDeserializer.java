package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.io.IOException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongAsUuidEncryptorDeserializer extends StdDeserializer<Long> {

    public LongAsUuidEncryptorDeserializer() {
        this(null);
    }

    public LongAsUuidEncryptorDeserializer(Class<Long> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
        return EncryptorsHolder.decryptLongAsUUID(jp.getText());
    }
}
