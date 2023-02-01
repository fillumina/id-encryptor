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

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongAsUuidEncryptorSerializer extends StdSerializer<Long>
        implements ContextualSerializer {

    private long fieldIndex = 0;

    public LongAsUuidEncryptorSerializer(long fieldIndex) {
        this(null);
        this.fieldIndex = fieldIndex;
    }

    public LongAsUuidEncryptorSerializer() {
        this(null);
    }

    public LongAsUuidEncryptorSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        jgen.writeString(EncryptorsHolder.encryptLongAsUuid(fieldIndex, (long)value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        EncryptableLongAsUuid ann = property.getAnnotation(EncryptableLongAsUuid.class);
        if (ann != null) {
            return new LongAsUuidEncryptorSerializer(ann.value());
        }
        EncryptableLongAsUuidCollection annColl =
                property.getAnnotation(EncryptableLongAsUuidCollection.class);
        if (annColl != null) {
            return new LongAsUuidEncryptorSerializer(annColl.value());
        }
        return null;
    }

}