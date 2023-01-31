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
public class LongAsUuidEncryptorSerializer extends StdSerializer<Long> {

    public LongAsUuidEncryptorSerializer() {
        this(null);
    }

    public LongAsUuidEncryptorSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        jgen.writeString(EncryptorsHolder.encryptLongAsUUID((long)value));
    }
}