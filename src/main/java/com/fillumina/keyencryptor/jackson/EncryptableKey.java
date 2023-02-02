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
@JsonSerialize(keyUsing = EncryptorKeySerializer.class)
@JsonDeserialize(keyUsing = EncryptorKeyDeserializer.class)
public @interface EncryptableKey {

    /**
     * Defines a seed to use to scramble sequences from different fields so they
     * will not have the same encrypted string. It does not apply to UUIDs that
     * should be already generated unique by a shared generator and are immune
     * to this problem.
     */
    public long value() default 0;

}
