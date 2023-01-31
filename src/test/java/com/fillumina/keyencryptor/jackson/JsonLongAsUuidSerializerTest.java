package com.fillumina.keyencryptor.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class JsonLongAsUuidSerializerTest {
    private static final long LONG_VALUE_1 = 123L;
    private static final long LONG_VALUE_2 = 456L;
    private static final String STRING_MAP_VALUE_1 = "xxxx";
    private static final String STRING_MAP_VALUE_2 = "yyyy";

    private static final List<Long> LONG_LIST = List.of(LONG_VALUE_2, LONG_VALUE_2);
    private static final Map<Long, String> LONG_MAP = Map.of(
            LONG_VALUE_1, STRING_MAP_VALUE_1, LONG_VALUE_2, STRING_MAP_VALUE_2);

    public static class Bean {
        @EncryptableLongAsUuid
        Long encryptableLongValue = LONG_VALUE_1;

        @EncryptableLongAsUuid(1)
        Long encryptableLongValue1 = LONG_VALUE_1;

        @EncryptableLongAsUuid(2)
        Long encryptableLongValue2 = LONG_VALUE_1;

        Long nonEncryptableLongValue = LONG_VALUE_1;

        @EncryptableLongAsUuidCollection
        List<Long> encryptableLongList = LONG_LIST;

        @EncryptableLongAsUuidCollection(1)
        List<Long> encryptableLongList1 = LONG_LIST;

        @EncryptableLongAsUuidCollection(2)
        List<Long> encryptableLongList2 = LONG_LIST;

        List<Long> nonEncryptableLongList = LONG_LIST;

        @EncryptableLongAsUuidKey
        Map<Long, String> encryptableLongMap = LONG_MAP;

        @EncryptableLongAsUuidKey(1)
        Map<Long, String> encryptableLongMap1 = LONG_MAP;

        @EncryptableLongAsUuidKey(2)
        Map<Long, String> encryptableLongMap2 = LONG_MAP;

        Map<Long, String> nonEncryptableLongMap = LONG_MAP;
    }

    public static class SerializedBean {
        String encryptableLongValue;
        String encryptableLongValue1;
        String encryptableLongValue2;
        Long nonEncryptableLongValue;

        List<String> encryptableLongList;
        List<String> encryptableLongList1;
        List<String> encryptableLongList2;
        List<Long> nonEncryptableLongList;

        Map<String, String> encryptableLongMap;
        Map<String, String> encryptableLongMap1;
        Map<String, String> encryptableLongMap2;
        Map<Long, String> nonEncryptableLongMap;
    }

    private Bean originalBean = new Bean();
    private final SerializedBean encryptedBean;
    private final String serialized;
    private final Bean decryptedBean;

    public JsonLongAsUuidSerializerTest() throws JsonProcessingException {
        EncryptorsHolder.initEncryptorsWithPassword("abracadabra");

        final ObjectMapper objectMapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.serialized = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalBean);
        this.encryptedBean = objectMapper.readValue(serialized, SerializedBean.class);
        this.decryptedBean = objectMapper.readValue(serialized, Bean.class);

        //System.out.println("SERIALIZED: " + serialized);
    }

    @Test
    public void shouldSerializeSingleLongValue() throws Exception {
        assertNotEquals(originalBean.encryptableLongValue, encryptedBean.encryptableLongValue);
        assertNotEquals(originalBean.encryptableLongValue1, encryptedBean.encryptableLongValue1);
        assertNotEquals(originalBean.encryptableLongValue2, encryptedBean.encryptableLongValue2);

        assertNotEquals(encryptedBean.encryptableLongValue, encryptedBean.encryptableLongValue1);
        assertNotEquals(encryptedBean.encryptableLongValue, encryptedBean.encryptableLongValue2);
        assertNotEquals(encryptedBean.encryptableLongValue1, encryptedBean.encryptableLongValue2);

        assertEquals(originalBean.nonEncryptableLongValue, encryptedBean.nonEncryptableLongValue);

        assertEquals(originalBean.encryptableLongValue, decryptedBean.encryptableLongValue);
        assertEquals(originalBean.encryptableLongValue1, decryptedBean.encryptableLongValue1);
        assertEquals(originalBean.encryptableLongValue2, decryptedBean.encryptableLongValue2);
        assertEquals(originalBean.nonEncryptableLongValue, decryptedBean.nonEncryptableLongValue);
    }

    @Test
    public void shouldSerializeLongLists() throws Exception {
        assertNotEquals(originalBean.encryptableLongList, encryptedBean.encryptableLongList);
        assertNotEquals(originalBean.encryptableLongList1, encryptedBean.encryptableLongList1);
        assertNotEquals(originalBean.encryptableLongList2, encryptedBean.encryptableLongList2);

        assertNotEquals(encryptedBean.encryptableLongList, encryptedBean.encryptableLongList1);
        assertNotEquals(encryptedBean.encryptableLongList, encryptedBean.encryptableLongList2);
        assertNotEquals(encryptedBean.encryptableLongList1, encryptedBean.encryptableLongList2);

        assertEquals(originalBean.nonEncryptableLongList, encryptedBean.nonEncryptableLongList);

        assertEquals(originalBean.encryptableLongList, decryptedBean.encryptableLongList);
        assertEquals(originalBean.encryptableLongList1, decryptedBean.encryptableLongList1);
        assertEquals(originalBean.encryptableLongList2, decryptedBean.encryptableLongList2);
        assertEquals(originalBean.nonEncryptableLongList, decryptedBean.nonEncryptableLongList);
    }

    @Test
    public void shouldSerializeLongMapKeys() throws Exception {
        assertNotEquals(originalBean.encryptableLongMap, encryptedBean.encryptableLongMap);
        assertNotEquals(originalBean.encryptableLongMap1, encryptedBean.encryptableLongMap1);
        assertNotEquals(originalBean.encryptableLongMap2, encryptedBean.encryptableLongMap2);

        assertNotEquals(encryptedBean.encryptableLongMap, encryptedBean.encryptableLongMap1);
        assertNotEquals(encryptedBean.encryptableLongMap, encryptedBean.encryptableLongMap2);
        assertNotEquals(encryptedBean.encryptableLongMap1, encryptedBean.encryptableLongMap2);

        assertEquals(originalBean.nonEncryptableLongMap, encryptedBean.nonEncryptableLongMap);

        assertEquals(originalBean.encryptableLongMap, decryptedBean.encryptableLongMap);
        assertEquals(originalBean.encryptableLongMap1, decryptedBean.encryptableLongMap1);
        assertEquals(originalBean.encryptableLongMap2, decryptedBean.encryptableLongMap2);
        assertEquals(originalBean.nonEncryptableLongMap, decryptedBean.nonEncryptableLongMap);
    }

}
