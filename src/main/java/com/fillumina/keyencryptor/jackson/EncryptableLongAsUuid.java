package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = LongAsUuidEncryptorSerializer.class)
@JsonDeserialize(using = LongAsUuidEncryptorDeserializer.class)
public @interface EncryptableLongAsUuid {
    public long value() default 0;
}
