package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JsonSerializerTest {
    private static final long LONG_1 = 123L;
    private static final long LONG_2 = 456L;

    private static final String STRING_1 = "xxxx";
    private static final String STRING_2 = "yyyy";

    private static final UUID UUID_1 = UUID.fromString("9786f121-209a-4f16-8d4a-2ef306d74a8d");
    private static final UUID UUID_2 = UUID.fromString("7e3bd548-6494-45f7-a5d8-548e3852bc7c");

    private Bean originalBean = new Bean(LONG_1, LONG_2, UUID_1, UUID_2, STRING_1, STRING_2);
    private final SerializedBean encryptedBean;
    private final String serialized;
    private final Bean decryptedBean;

    public JsonSerializerTest() throws JsonProcessingException {
        EncryptorsHolder.initEncryptorsWithPassword("abracadabra");

        final ObjectMapper objectMapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.serialized = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalBean);
        this.encryptedBean = objectMapper.readValue(serialized, SerializedBean.class);
        this.decryptedBean = objectMapper.readValue(serialized, Bean.class);

        // System.out.println("SERIALIZED: " + serialized);
    }

    @Test
    public void shouldEncryptLongNullValueToNull() {
        assertNull(originalBean.encryptableLongNullValue);
        assertNull(encryptedBean.encryptableLongNullValue);
        assertNull(decryptedBean.encryptableLongNullValue);
    }

    @Test
    public void shouldEncryptLong52NullValueToNull() {
        assertNull(originalBean.encryptableLong52NullValue);
        assertNull(encryptedBean.encryptableLong52NullValue);
        assertNull(decryptedBean.encryptableLong52NullValue);
    }

    @Test
    public void shouldSerializeSingleLongValue() throws Exception {
        assertNotEquals(originalBean.encryptableLongValue, encryptedBean.encryptableLongValue);
        assertEquals(originalBean.nonEncryptableLongValue, encryptedBean.nonEncryptableLongValue);

        assertEquals(originalBean.encryptableLongValue, decryptedBean.encryptableLongValue);
        assertEquals(originalBean.nonEncryptableLongValue, decryptedBean.nonEncryptableLongValue);
    }

    @Test
    public void shouldSerializedSingleLongValueWithDifferentSeeds() throws Exception {
        assertNotEquals(encryptedBean.encryptableLongValue, encryptedBean.encryptableLongValue2);
        assertNotEquals(encryptedBean.encryptableLongValue, encryptedBean.encryptableLongValue3);
        assertNotEquals(encryptedBean.encryptableLongValue2, encryptedBean.encryptableLongValue3);

        assertEquals(originalBean.encryptableLongValue, decryptedBean.encryptableLongValue);
        assertEquals(originalBean.encryptableLongValue2, decryptedBean.encryptableLongValue2);
        assertEquals(originalBean.encryptableLongValue3, decryptedBean.encryptableLongValue3);
    }

    @Test
    public void shouldSerializeLong52() {
        assertNotEquals(originalBean.encryptableLong52Value, encryptedBean.encryptableLong52Value);
        assertEquals(originalBean.encryptableLong52Value, decryptedBean.encryptableLong52Value);
    }

    @Test
    public void shouldSerializeLongAsUuid() {
        assertNotEquals(originalBean.encryptableLongAsUuidValue, encryptedBean.encryptableLongAsUuidValue);
        assertEquals(originalBean.encryptableLongAsUuidValue, decryptedBean.encryptableLongAsUuidValue);
    }

    @Test
    public void shouldSerializeLongLists() throws Exception {
        assertNotEquals(originalBean.encryptableLongList, encryptedBean.encryptableLongList);
        assertEquals(originalBean.nonEncryptableLongList, encryptedBean.nonEncryptableLongList);

        assertEquals(originalBean.encryptableLongList, decryptedBean.encryptableLongList);
        assertEquals(originalBean.nonEncryptableLongList, decryptedBean.nonEncryptableLongList);
    }

    @Test
    public void shouldSerializableLongListWithDifferentSeeds() throws Exception {
        assertNotEquals(encryptedBean.encryptableLongList, encryptedBean.encryptableLongList2);
        assertNotEquals(originalBean.encryptableLongList2, encryptedBean.encryptableLongList2);

        assertEquals(originalBean.encryptableLongList2, decryptedBean.encryptableLongList2);
    }

    @Test
    public void shouldSerializeLong52List() {
        assertNotEquals(originalBean.encryptableLong52List, encryptedBean.encryptableLong52List);
        assertEquals(originalBean.encryptableLong52List, decryptedBean.encryptableLong52List);
    }

    @Test
    public void shouldSerializeLongAsUuidList() {
        assertNotEquals(originalBean.encryptableLongAsUuidList, encryptedBean.encryptableLongAsUuidList);
        assertEquals(originalBean.encryptableLongAsUuidList, decryptedBean.encryptableLongAsUuidList);
    }

    @Test
    public void shouldSerializeLongMapKeys() throws Exception {
        assertNotEquals(originalBean.encryptableLongMap, encryptedBean.encryptableLongMap);
        assertEquals(originalBean.nonEncryptableLongMap, encryptedBean.nonEncryptableLongMap);

        assertEquals(originalBean.encryptableLongMap, decryptedBean.encryptableLongMap);
        assertEquals(originalBean.nonEncryptableLongMap, decryptedBean.nonEncryptableLongMap);
    }

    @Test
    public void shouldSerializeLongMapKeysWithDifferentSeeds() {
        assertNotEquals(encryptedBean.encryptableLongMap, encryptedBean.encryptableLongMap2);
        assertNotEquals(originalBean.encryptableLongMap2, encryptedBean.encryptableLongMap2);

        assertEquals(originalBean.encryptableLongMap2, decryptedBean.encryptableLongMap2);
    }

    @Test
    public void shouldSerializeLong52Map() {
        assertNotEquals(originalBean.encryptableLong52Map, encryptedBean.encryptableLong52Map);
        assertEquals(originalBean.encryptableLong52Map, decryptedBean.encryptableLong52Map);
    }

    @Test
    public void shouldSerializeLongAsUuidMap() {
        assertNotEquals(originalBean.encryptableLongAsUuidMap, encryptedBean.encryptableLongAsUuidMap);
        assertEquals(originalBean.encryptableLongAsUuidMap, decryptedBean.encryptableLongAsUuidMap);
    }

    @Test
    public void shouldSerializeSingleUUIDValue() throws Exception {
        assertNotEquals(originalBean.encryptableUuidValue, encryptedBean.encryptableUuidValue);
        assertEquals(originalBean.nonEncryptableUuidValue, encryptedBean.nonEncryptableUuidValue);

        assertEquals(originalBean.encryptableUuidValue, decryptedBean.encryptableUuidValue);
        assertEquals(originalBean.nonEncryptableUuidValue, decryptedBean.nonEncryptableUuidValue);
    }

    @Test
    public void shouldSerializeUUIDLists() throws Exception {
        assertNotEquals(originalBean.encryptableUuidList, encryptedBean.encryptableUuidList);
        assertEquals(originalBean.nonEncryptableUuidList, encryptedBean.nonEncryptableUuidList);

        assertEquals(originalBean.encryptableUuidList, decryptedBean.encryptableUuidList);
        assertEquals(originalBean.nonEncryptableUuidList, decryptedBean.nonEncryptableUuidList);
    }

    @Test
    public void shouldSerializeUUIDMapKeys() throws Exception {
        assertNotEquals(originalBean.encryptableUuidMap, encryptedBean.encryptableUuidMap);
        assertEquals(originalBean.nonEncryptableUuidMap, encryptedBean.nonEncryptableUuidMap);

        assertEquals(originalBean.encryptableUuidMap, decryptedBean.encryptableUuidMap);
        assertEquals(originalBean.nonEncryptableUuidMap, decryptedBean.nonEncryptableUuidMap);
    }

}
