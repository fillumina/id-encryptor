package com.fillumina.keyencryptor.jackson;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class Bean {


    @Encryptable(type = ExportType.String)
    Long encryptableLongNullValue;

    @Encryptable(type = ExportType.Long52Bit)
    Long encryptableLong52NullValue;

    // serialized as an encrypted string
    @Encryptable(type = ExportType.String)
    Long encryptableLongValue;

    // uses a nodeId=2 to encrypt to a different string than the previous
    @Encryptable(type = ExportType.String, nodeId = 2)
    Long encryptableLongValue2;

    // uses a nodeId=3 to encrypt to a different string than the previous
    @Encryptable(type = ExportType.String, nodeId = 3)
    Long encryptableLongValue3;

    // encrypts to a long number of 52 bit max usable by javascript
    @Encryptable(type = ExportType.Long52Bit)
    Long encryptableLong52Value;

    // encrypts to a UUID created from the long id
    // (optionally you can set a nodeId)
    @Encryptable(type = ExportType.LongAsUuid)
    Long encryptableLongAsUuidValue;

    Long nonEncryptableLongValue;

    @EncryptableCollection(type = ExportType.String)
    List<Long> encryptableLongList;

    @EncryptableCollection(type = ExportType.String, nodeId = 2)
    List<Long> encryptableLongList2;

    @EncryptableCollection(type = ExportType.Long52Bit)
    List<Long> encryptableLong52List;

    @EncryptableCollection(type = ExportType.Long52Bit)
    List<Long> encryptableLongAsUuidList;

    List<Long> nonEncryptableLongList;

    @EncryptableKey(type = ExportType.String)
    Map<Long, String> encryptableLongMap;

    @EncryptableKey(type = ExportType.String, nodeId = 2)
    Map<Long, String> encryptableLongMap2;

    @EncryptableKey(type = ExportType.Long52Bit)
    Map<Long, String> encryptableLong52Map;

    @EncryptableKey(type = ExportType.LongAsUuid)
    Map<Long, String> encryptableLongAsUuidMap;

    Map<Long, String> nonEncryptableLongMap;

    @Encryptable(type = ExportType.Uuid)
    UUID encryptableUuidValue;

    UUID nonEncryptableUuidValue;

    @EncryptableCollection(type = ExportType.Uuid)
    List<UUID> encryptableUuidList;

    List<UUID> nonEncryptableUuidList;

    @EncryptableKey(type = ExportType.Uuid)
    Map<UUID, String> encryptableUuidMap;

    Map<UUID, String> nonEncryptableUuidMap;

    public Bean() {
    }

    public Bean(Long long1, Long long2, UUID uuid1, UUID uuid2, String string1, String string2) {
        final List<Long> longList = List.of(long1, long2);
        final Map<Long, String> longMap = Map.of(long1, string1, long2, string2);
        final List<UUID> uuidList = List.of(uuid1, uuid2);
        final Map<UUID, String> uuidMap = Map.of(uuid1, string1, uuid2, string2);

        encryptableLongValue = long1;
        encryptableLongValue2 = long1;
        encryptableLongValue3 = long1;
        encryptableLong52Value = long1;
        encryptableLongAsUuidValue = long1;
        nonEncryptableLongValue = long1;
        encryptableLongList = longList;
        encryptableLongList2 = longList;
        encryptableLong52List = longList;
        encryptableLongAsUuidList = longList;
        nonEncryptableLongList = longList;
        encryptableLongMap = longMap;
        encryptableLongMap2 = longMap;
        encryptableLong52Map = longMap;
        encryptableLongAsUuidMap = longMap;
        nonEncryptableLongMap = longMap;
        encryptableUuidValue = uuid1;
        nonEncryptableUuidValue = uuid2;
        encryptableUuidList = uuidList;
        nonEncryptableUuidList = uuidList;
        encryptableUuidMap = uuidMap;
        nonEncryptableUuidMap = uuidMap;
    }

}
