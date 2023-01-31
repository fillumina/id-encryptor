package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeySerializer extends StdSerializer<Object> {

    public EncryptorKeySerializer() {
        this(null);
    }

    public EncryptorKeySerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        Class<?> clazz = value.getClass();
        if (clazz == Long.class) {
            jgen.writeFieldName(EncryptorsHolder.encryptLong((long)value));
        } else if (clazz == UUID.class) {
            jgen.writeFieldName(EncryptorsHolder.encryptUuid((UUID) value));
        }
    }
}