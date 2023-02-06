package com.fillumina.idencryptor.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JacksonTest {

    public static class A {
        String value;
    }

    public static class B {
        String value;
    }

    @Test
    public void shouldDecodeInSimilarClass() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        /*
        For SpringBoot use the following setup in properties:

        jackson:
            visibility.field: any
            visibility.getter: none
            visibility.setter: none
            visibility.is-getter: none
        */

        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        A a = new A();
        a.value = "aaaa";

        String serialized = objectMapper.writeValueAsString(a);
        B b = objectMapper.readValue(serialized, B.class);

        assertEquals(a.value, b.value);
    }
}
