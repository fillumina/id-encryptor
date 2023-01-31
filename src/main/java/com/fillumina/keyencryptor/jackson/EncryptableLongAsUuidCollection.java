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
@JsonSerialize(contentUsing = LongAsUuidEncryptorSerializer.class)
@JsonDeserialize(contentUsing = LongAsUuidEncryptorDeserializer.class)
public @interface EncryptableLongAsUuidCollection {
    public long value() default 0;
}
