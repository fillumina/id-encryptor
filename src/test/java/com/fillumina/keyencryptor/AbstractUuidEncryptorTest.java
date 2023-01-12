package com.fillumina.keyencryptor;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractUuidEncryptorTest {

    public abstract UuidEncryptor createEncryptor(String key);

    @Test
    public void shouldEncryptAndDecrypt() {
        final UuidEncryptor cipher = createEncryptor("dAtAbAsE98765432");
        for (int i=0; i<100; i++) {
            UUID uuid = UUID.randomUUID();
            UUID encrypted = cipher.encrypt(uuid);
            assertNotEquals(uuid, encrypted);
            UUID decrypted = cipher.decrypt(encrypted);
            assertEquals(uuid, decrypted);
        }
    }

}
