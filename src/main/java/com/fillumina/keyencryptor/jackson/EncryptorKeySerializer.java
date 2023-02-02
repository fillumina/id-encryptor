package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeySerializer extends StdSerializer<Object> implements ContextualSerializer {

    private long seed;

    public EncryptorKeySerializer(long seed) {
        this(null);
        this.seed = seed;
    }

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
            jgen.writeFieldName(EncryptorsHolder.encryptLong(seed, (long)value));
        } else if (clazz == UUID.class) {
            jgen.writeFieldName(EncryptorsHolder.encryptUuid((UUID) value));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        EncryptableKey ann = property.getAnnotation(EncryptableKey.class);
        if (ann != null) {
            return new EncryptorKeySerializer(ann.value());
        }
        return null;
    }

}