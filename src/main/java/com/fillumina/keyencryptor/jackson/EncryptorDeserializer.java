package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import static com.fillumina.keyencryptor.jackson.ExportType.Long52Bit;
import static com.fillumina.keyencryptor.jackson.ExportType.Uuid;
import java.io.IOException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {

    private long seed;
    private ExportType exportType;

    public EncryptorDeserializer(long seed, ExportType exportType) {
        this(null);
        this.seed = seed;
        this.exportType = exportType;
    }

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
            return EncryptorsHolder.decryptLong(seed, encryptedString);
        } else {
            switch(exportType) {
                case Uuid:
                    return EncryptorsHolder.decryptUuid(encryptedString);
                case LongAsUuid:
                    return EncryptorsHolder.decryptLongAsUuid(encryptedString);
                case Long52Bit:
                    return EncryptorsHolder.decryptEncodedLong52(Long.parseLong(encryptedString));
                case Long:
                    return EncryptorsHolder.decryptEncodedLong(Long.parseLong(encryptedString));
            }
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        Encryptable ann = property.getAnnotation(Encryptable.class);
        if (ann != null) {
            return new EncryptorDeserializer(ann.nodeId(), ann.type());
        }
        EncryptableCollection annColl = property.getAnnotation(EncryptableCollection.class);
        if (annColl != null) {
            return new EncryptorDeserializer(annColl.nodeId(), annColl.type());
        }
        return null;
    }
}
