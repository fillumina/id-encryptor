package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorSerializer extends StdSerializer<Object> {

    public EncryptorSerializer() {
        this(null);
    }

    public EncryptorSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        Class<?> clazz = value.getClass();
        if (clazz == Long.class) {
            Long encryptedLong = EncryptorsHolder.getLongEncryptor().encrypt((long)value);
            String encryptedString = LongCrockfordConverter.toString(encryptedLong);
            jgen.writeString(encryptedString);
        } else if (clazz == UUID.class) {
            UUID encryptedUuid = EncryptorsHolder.getUuidEncryptor().encrypt((UUID)value);
            jgen.writeString(encryptedUuid.toString());
        }
    }
}