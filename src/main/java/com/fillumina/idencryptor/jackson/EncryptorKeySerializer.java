package com.fillumina.idencryptor.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fillumina.idencryptor.EncryptorsHolder;
import static com.fillumina.idencryptor.jackson.ExportType.Long52Bit;
import static com.fillumina.idencryptor.jackson.ExportType.LongAsUuid;
import static com.fillumina.idencryptor.jackson.ExportType.String;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeySerializer extends StdSerializer<Object> implements ContextualSerializer {

    private long seed;
    private ExportType exportType;

    public EncryptorKeySerializer(long seed, ExportType exportType) {
        this(null);
        this.seed = seed;
        this.exportType = exportType;
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
            switch(exportType) {
                case String:
                    jgen.writeFieldName(EncryptorsHolder.encryptLong(seed, (long)value));
                    break;
                case LongAsUuid:
                    jgen.writeFieldName(EncryptorsHolder.encryptLongAsUuid(seed, (long)value));
                    break;
                case Long52Bit:
                    jgen.writeFieldId(EncryptorsHolder.encryptEncodedLong52((long)value));
                    break;
                case Long:
                    jgen.writeFieldId(EncryptorsHolder.encryptEncodedLong((long)value));
                    break;
            }
        } else if (clazz == UUID.class) {
            jgen.writeFieldName(EncryptorsHolder.encryptUuid((UUID) value));
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        EncryptableKey ann = property.getAnnotation(EncryptableKey.class);
        if (ann != null) {
            return new EncryptorKeySerializer(ann.nodeId(), ann.type());
        }
        return null;
    }

}