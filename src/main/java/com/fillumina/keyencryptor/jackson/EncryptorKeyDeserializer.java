package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fillumina.keyencryptor.EncryptorsHolder;
import com.fillumina.keyencryptor.LongCrockfordConverter;
import static com.fillumina.keyencryptor.jackson.ExportType.Long52Bit;
import static com.fillumina.keyencryptor.jackson.ExportType.LongAsUuid;
import static com.fillumina.keyencryptor.jackson.ExportType.Uuid;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorKeyDeserializer extends KeyDeserializer implements ContextualKeyDeserializer {

    private long seed;
    private ExportType exportType;

    public EncryptorKeyDeserializer(long seed, ExportType exportType) {
        this();
        this.seed = seed;
        this.exportType = exportType;
    }

    public EncryptorKeyDeserializer() {
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        String encryptedString = key;
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
    public KeyDeserializer createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        EncryptableKey ann = property.getAnnotation(EncryptableKey.class);
        if (ann != null) {
            return new EncryptorKeyDeserializer(ann.nodeId(), ann.type());
        }
        return null;
    }

}
