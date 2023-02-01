package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolderTest {

    @Test
    public void shouldReturnFalseIfAlreadyPresentBuilder() {

        EncryptorsHolder.builder().build();

        assertFalse(EncryptorsHolder.builder().build());
    }

}
