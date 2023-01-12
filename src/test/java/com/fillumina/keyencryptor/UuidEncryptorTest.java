package com.fillumina.keyencryptor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UuidEncryptorTest extends AbstractUuidEncryptorTest {

    public static void main(String[] args) {
        UUID encrypted = new UuidEncryptor("123456789xyz").encrypt(UUID.randomUUID());
        System.out.println("UUID: " + encrypted.toString());
        System.out.println("version: " + encrypted.version());
        System.out.println("variant: " + encrypted.variant());
    }

    public UuidEncryptor createEncryptor(String key) {
        return new UuidEncryptor(key);
    }

    @Test
    public void shouldAnEncryptedUUIDHaveRandomVersionAndVariant() {
        Set<Integer> versionSet = new HashSet<>();
        Set<Integer> variantSet = new HashSet<>();
        for (int i=0; i<1000; i++) {
            UUID encrypted = createEncryptor("123456789xyz").encrypt(UUID.randomUUID());
            versionSet.add(encrypted.version());
            variantSet.add(encrypted.variant());
        }
        assertTrue(versionSet.size() > 1);
        assertTrue(variantSet.size() > 1);
    }
}
