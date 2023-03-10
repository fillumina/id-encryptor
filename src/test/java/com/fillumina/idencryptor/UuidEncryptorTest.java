package com.fillumina.idencryptor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorTest {

    public static void main(String[] args) {
        System.out.println("UUID: " + new UUID(1L, 2L).toString());

        UUID encrypted = new UuidEncryptor("123456789xyz").encrypt(UUID.randomUUID());
        System.out.println("UUID: " + encrypted.toString());
        System.out.println("version: " + encrypted.version());
        System.out.println("variant: " + encrypted.variant());
    }

    @Test
    public void shouldEncryptAndDecrypt() {
        final UuidEncryptor cipher = new UuidEncryptor("dAtAbAsE98765432");
        for (int i=0; i<100; i++) {
            UUID uuid = UUID.randomUUID();
            UUID encrypted = cipher.encrypt(uuid);
            assertNotEquals(uuid, encrypted);
            UUID decrypted = cipher.decrypt(encrypted);
            assertEquals(uuid, decrypted);
        }
    }

    @Test
    public void shouldAnEncryptedUUIDHaveRandomVersionAndVariant() {
        Set<Integer> versionSet = new HashSet<>();
        Set<Integer> variantSet = new HashSet<>();
        for (int i=0; i<1000; i++) {
            UUID encrypted = new UuidEncryptor("123456789xyz").encrypt(UUID.randomUUID());
            versionSet.add(encrypted.version());
            variantSet.add(encrypted.variant());
        }
        assertTrue(versionSet.size() > 1);
        assertTrue(variantSet.size() > 1);
    }
}
