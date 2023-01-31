package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.io.IOException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongAsUuidEncryptorKeySerializer extends StdSerializer<Long> {

    public LongAsUuidEncryptorKeySerializer() {
        this(null);
    }

    public LongAsUuidEncryptorKeySerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        jgen.writeFieldName(EncryptorsHolder.encryptLongAsUUID((long)value));
    }
}