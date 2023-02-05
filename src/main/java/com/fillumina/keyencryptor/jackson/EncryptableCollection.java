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
@JsonSerialize(contentUsing = EncryptorSerializer.class)
@JsonDeserialize(contentUsing = EncryptorDeserializer.class)
public @interface EncryptableCollection {

    public long nodeId() default 0;

    public ExportType type() default ExportType.String;
}
