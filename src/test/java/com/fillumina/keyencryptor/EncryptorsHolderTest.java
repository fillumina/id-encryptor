package com.fillumina.keyencryptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolderTest {

    @Test
    public void shouldReturnTrueIfInitialized() {

        EncryptorsHolder.builder().build();

        assertTrue(EncryptorsHolder.isInitialized());
    }

}
