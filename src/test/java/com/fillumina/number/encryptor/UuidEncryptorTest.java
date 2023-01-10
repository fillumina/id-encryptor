package com.fillumina.number.encryptor;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorTest {

    @Test
    public void shouldEncryptAndDecrypt() {
        final UuidEncryptor cipher = new UuidEncryptor("dAtAbAsE98765432");
        UUID uuid = UUID.randomUUID();
        UUID encrypted = cipher.encrypt(uuid);
        assertNotEquals(uuid, encrypted);
        UUID decrypted = cipher.decrypt(encrypted);
        assertEquals(uuid, decrypted);
    }
}
