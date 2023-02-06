package com.fillumina.idencryptor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RandomGenerationFromSequentialIdTest {

    private static final int SIZE = 100_000;

    @Test
    public void shouldNotConflictInSimpleLongGenerations() {
        Set<Long> set = new HashSet<>(SIZE);
        LongEncryptor enc1 = new LongEncryptor("helloworld");
        for (int i=0; i<SIZE; i++) {
            final Long value = enc1.encrypt((long)i);
            assertTrue(set.add(value));
        }

        int counter = 0;
        LongEncryptor enc2 = new LongEncryptor("heidisupeimonti");
        for (int i=0; i<SIZE; i++) {
            final Long value = enc2.encrypt((long)i);
            if (set.contains(value)) {
                //System.out.println("found " + value + " at index: " + i);
                counter++;
            }
        }

        // if that happens you've got that minuscole probability, lucky you!
        assertEquals(0, counter);
        //System.out.println("FOUND " + counter);
    }

    @Test
    public void shouldNotConflictInSimpleUUIDGenerations() {
        Set<UUID> set = new HashSet<>(SIZE);
        UuidEncryptor enc1 = new UuidEncryptor("helloworld");
        for (int i=0; i<SIZE; i++) {
            final UUID uuid = new UUID(123L, (long)i);
            final UUID value = enc1.encrypt(uuid);
            assertTrue(set.add(value));
        }

        int counter = 0;
        UuidEncryptor enc2 = new UuidEncryptor("heidisupeimonti");
        for (int i=0; i<SIZE; i++) {
            final UUID uuid = new UUID(123L, (long)i);
            final UUID value = enc2.encrypt(uuid);
            if (set.contains(value)) {
                //System.out.println("found " + value + " at index: " + i);
                counter++;
            }
        }

        // if that happens you've got that minuscole probability, lucky you!
        assertEquals(0, counter);
        //System.out.println("FOUND " + counter);
    }

}
