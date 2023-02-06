package com.fillumina.keyencryptor.jackson;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SerializedBean {
    String encryptableLongNullValue;
    Long encryptableLong52NullValue;

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
