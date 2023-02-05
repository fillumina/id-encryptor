package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JsonSerializerTest {
    private static final long LONG_VALUE_1 = 123L;
    private static final long LONG_VALUE_2 = 456L;
    private static final String STRING_MAP_VALUE_1 = "xxxx";
    private static final String STRING_MAP_VALUE_2 = "yyyy";

    private static final List<Long> LONG_LIST = List.of(LONG_VALUE_1, LONG_VALUE_2);
    private static final Map<Long, String> LONG_MAP = Map.of(
            LONG_VALUE_1, STRING_MAP_VALUE_1, LONG_VALUE_2, STRING_MAP_VALUE_2);

    private static final UUID UUID_VALUE_1 = UUID.fromString("9786f121-209a-4f16-8d4a-2ef306d74a8d");
    private static final UUID UUID_VALUE_2 = UUID.fromString("7e3bd548-6494-45f7-a5d8-548e3852bc7c");
    private static final List<UUID> UUID_LIST = List.of(UUID_VALUE_1, UUID_VALUE_2);
    private static final Map<UUID, String> UUID_MAP = Map.of(
            UUID_VALUE_1, STRING_MAP_VALUE_1, UUID_VALUE_2, STRING_MAP_VALUE_2);

    public static class Bean {
        // serialized as an encrypted string
        @Encryptable(type = ExportType.String)
        Long encryptableLongValue = LONG_VALUE_1;

        // uses a nodeId=2 to encrypt to a different string than the previous
        @Encryptable(type = ExportType.String, nodeId = 2)
        Long encryptableLongValue2 = LONG_VALUE_1;

        // uses a nodeId=3 to encrypt to a different string than the previous
        @Encryptable(type = ExportType.String, nodeId = 3)
        Long encryptableLongValue3 = LONG_VALUE_1;

        // encrypts to a long number of 52 bit max usable by javascript
        @Encryptable(type = ExportType.Long52Bit)
        Long encryptableLong52Value = LONG_VALUE_1;

        // encrypts to a UUID created from the long id
        // (optionally you can set a nodeId)
        @Encryptable(type = ExportType.LongAsUuid)
        Long encryptableLongAsUuidValue = LONG_VALUE_1;

        Long nonEncryptableLongValue = LONG_VALUE_1;

        @EncryptableCollection(type = ExportType.String)
        List<Long> encryptableLongList = LONG_LIST;

        @EncryptableCollection(type = ExportType.String, nodeId = 2)
        List<Long> encryptableLongList2 = LONG_LIST;

        @EncryptableCollection(type = ExportType.Long52Bit)
        List<Long> encryptableLong52List = LONG_LIST;

        @EncryptableCollection(type = ExportType.Long52Bit)
        List<Long> encryptableLongAsUuidList = LONG_LIST;

        List<Long> nonEncryptableLongList = LONG_LIST;

        @EncryptableKey(type = ExportType.String)
        Map<Long, String> encryptableLongMap = LONG_MAP;

        @EncryptableKey(type = ExportType.String, nodeId = 2)
        Map<Long, String> encryptableLongMap2 = LONG_MAP;

        @EncryptableKey(type = ExportType.Long52Bit)
        Map<Long, String> encryptableLong52Map = LONG_MAP;

        @EncryptableKey(type = ExportType.LongAsUuid)
        Map<Long, String> encryptableLongAsUuidMap = LONG_MAP;

        Map<Long, String> nonEncryptableLongMap = LONG_MAP;

        @Encryptable(type = ExportType.Uuid)
        UUID encryptableUuidValue = UUID_VALUE_1;

        UUID nonEncryptableUuidValue = UUID_VALUE_2;

        @EncryptableCollection(type = ExportType.Uuid)
        List<UUID> encryptableUuidList = UUID_LIST;

        List<UUID> nonEncryptableUuidList = UUID_LIST;

        @EncryptableKey(type = ExportType.Uuid)
        Map<UUID, String> encryptableUuidMap = UUID_MAP;

        Map<UUID, String> nonEncryptableUuidMap = UUID_MAP;
    }

    public static class SerializedBean {
        String encryptableLongValue;
        String encryptableLongValue2;
        String encryptableLongValue3;
        Long encryptableLong52Value;
        String encryptableLongAsUuidValue;
        Long nonEncryptableLongValue;

        List<String> encryptableLongList;
        List<String> encryptableLongList2;
        List<Long> encryptableLong52List;
        List<String> encryptableLongAsUuidList;
        List<Long> nonEncryptableLongList;

        Map<String, String> encryptableLongMap;
        Map<String, String> encryptableLongMap2;
        Map<Long, String> encryptableLong52Map;
        Map<String, String> encryptableLongAsUuidMap;
        Map<Long, String> nonEncryptableLongMap;

        String encryptableUuidValue;
        UUID nonEncryptableUuidValue;

        List<String> encryptableUuidList;
        List<UUID> nonEncryptableUuidList;

        Map<String, String> encryptableUuidMap;
        Map<UUID, String> nonEncryptableUuidMap;
    }

    private Bean originalBean = new Bean();
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
