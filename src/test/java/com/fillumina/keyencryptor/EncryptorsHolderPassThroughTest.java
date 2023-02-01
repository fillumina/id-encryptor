package com.fillumina.keyencryptor;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class EncryptorsHolderPassThroughTest {

    public static void main(String[] args) {
        EncryptorsHolder.initEncryptorsAsPassThrough();

        String a = EncryptorsHolder.encryptLongAsUuid(1L, 1L);
        System.out.println("a: " + a);

    }

    public EncryptorsHolderPassThroughTest() {
        EncryptorsHolder.initEncryptorsAsPassThrough();
    }

    @Test
    public void shouldCreateLongAsUuid() {
        assertEquals("00000000-0000-0000-0000-000000000000",
                EncryptorsHolder.encryptLongAsUuid(0, 0L));
        assertEquals("00000000-0000-0000-0000-000000000001",
                EncryptorsHolder.encryptLongAsUuid(0, 1L));
        assertEquals("00000000-0000-0000-0000-000000000002",
                EncryptorsHolder.encryptLongAsUuid(0, 2L));
    }

    @Test
    public void shouldCreateLongAsUuidWithNodeId() {
        assertEquals("00000000-0000-0000-0000-000000000001",
                EncryptorsHolder.encryptLongAsUuid(0, 1L));
        assertEquals("00000000-0000-0001-0000-000000000001",
                EncryptorsHolder.encryptLongAsUuid(1, 1L));
        assertEquals("00000000-0000-0002-0000-000000000001",
                EncryptorsHolder.encryptLongAsUuid(2, 1L));
    }


    @Test
    public void shouldCreateLong() {
        assertEquals("0000000000000",
                EncryptorsHolder.encryptLong(0L));
        assertEquals("0000000000001",
                EncryptorsHolder.encryptLong(1L));
        assertEquals("0000000000002",
                EncryptorsHolder.encryptLong(2L));
    }


    @Test
    public void shouldCreateUuid() {
        assertEquals("00000000-0000-0000-0000-000000000000",
                EncryptorsHolder.encryptUuid(new UUID(0, 0L)));
        assertEquals("00000000-0000-0000-0000-000000000001",
                EncryptorsHolder.encryptUuid(new UUID(0, 1L)));
        assertEquals("00000000-0000-0000-0000-000000000002",
                EncryptorsHolder.encryptUuid(new UUID(0, 2L)));
    }

}
