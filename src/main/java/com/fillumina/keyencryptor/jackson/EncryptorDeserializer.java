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
import java.io.IOException;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {

    private long seed;

    public EncryptorDeserializer(long seed) {
        this(null);
        this.seed = seed;
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
            return EncryptorsHolder.decryptUuid(encryptedString);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        Encryptable ann = property.getAnnotation(Encryptable.class);
        if (ann != null) {
            return new EncryptorDeserializer(ann.value());
        }
        EncryptableCollection annColl = property.getAnnotation(EncryptableCollection.class);
        if (annColl != null) {
            return new EncryptorDeserializer(annColl.value());
        }
        return null;
    }
}
