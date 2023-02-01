# ID Encryptor

Encrypts long values (64 bit) and UUIDs (128 bit).

## Scenario

A unit of data is addressed by an SQL Database using an identifier that might be *natural* (being part of the data) or *surrogate* (assigned externally). A commonly used surrogate identifier is a sequential number called ID. This method is simple, compact and fast but given IDs are not unique among a distributed system: once a system needs to talk with others the ID must be substituted with something more universal. One solution is to use very large random numbers: they can be assigned by any system autonomously and are guaranteed unique by their randomness.

### UUID

An [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) is basically a random number of almost 128 bit (some bits are reserved) big enough so the probability of two values are being created equal is *extremely* low (under some conditions and for any practical case they are *guaranteed unique*). Unfortunately the same randomness that makes them unique behave very inefficiently with the way most databases organize their indexes, the B-Tree, leading to horrible performances. To overcome this pitfall a new type of UUID has been created, the **sequential UUID**, that are generated as an increasing sequence of 128 bit numbers. Sequential UUIDs work well with database indexes but make UUID predictable once one element of the sequence is known. 

In case having a unique non guessable ID was a requirement **sequential UUIDs can be encrypted**: that is the purpose of this package.

### TSID

[TSID](https://github.com/f4b6a3/tsid-creator) is a smaller version of UUID that is only 64 bits long. It has the same features of a full blown UUID with a bit more predictability (less random bits available). Because the 64 bit TSID can be encoded in a single long value and is generated in sequence (again to fix the index problem) there is a **long encryptor** available in this package in case it needs to be scrambled. Note that even if the TSID is effectively a long value it doesn't fit into the [maximum number that javascript can accept as integer](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER) so it must be exported as string. The project doesn't refer directly to TSID for generality (they can be treated effectively as long).

## Exporting the default Long ID as a UUID

Because the encryptor works with long values it is perfectly legit to encrypt just a default sequential long ID to make the next sequence value hard to guess (and it would work at the cost of having to export them as string). 

It is also possible to create a `UUID` combining the long `id` with a fixed `nodeId` for each different node (and maybe with a sort of `fieldIndex` value if it is important to have different UUIDs for the same values of two different entities).

## Encryption

The proposed encryption algorithms are [Blowfish](https://en.wikipedia.org/wiki/Blowfish_(cipher)) for TSID (64 bit) and [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) for UUID (128 bit) used with the [ECB mode](https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm). ECB mode is discouraged for encrypting multi blocks messages but it's perfectly adequate if the data to encrypt matches the size of one single cipher block.

## Cache

Cached version of the encryptors is also provided to improve performances. It uses weak references so that cached memory could be reclaimed by the GC.

## Jackson serialized/ deserializer

The ID encryption should happen at the **API boundaries** and the serialization of data into [JSON](https://www.json.org/json-en.html) strings is a perfect place to encode them. An added benefit is that all the annotated indexes will be automatically translated back and forth (and long will be converted to string using the Crockford encoding as well to overcome the Javascript max integer number limitation). The project provides serialization and deserialization helper for both long and UUID when used as single fields, into lists or as key in a map. There are annotations provided as a shortcut as well.

```java
public static class Bean {
    // serialized as an encrypted string
    @Encryptable
    Long encryptableLongValue = 1;

    // serialized as an encrypted UUID with fieldId = 123
    @EncryptableLongAsUuid(123)
    Long encryptableLongAsUUIDValue1 = 1;

    // serialized as an encrypted UUID with fieldId = 456
    // the serialized UUID will be different than the previous one
    @EncryptableLongAsUuid(456)
    Long encryptableLongAsUUIDValue2 = 1;

    Long nonEncryptableLongValue = 1;

    // serialized as an encrypted string
    @EncryptableCollection
    List<Long> encryptableLongList;

    // serialized as an encrypted UUID with fieldId = 123
    @EncryptableLongAsUuidCollection(123)
    List<Long> encryptableLongAsUuidList;

    List<Long> nonEncryptableLongList;

    // serialized as an encrypted string
    @EncryptableKey
    Map<Long, String> encryptableLongMap;

    // serialized as an encrypted UUID with fieldId = 123
    @EncryptableLongAsUuidKey(123)
    Map<Long, String> encryptableLongAsUuidMap;

    Map<Long, String> nonEncryptableLongMap;

    @Encryptable
    UUID encryptableUuidValue;

    UUID nonEncryptableUuidValue;

    @EncryptableCollection
    List<UUID> encryptableUuidList;

    List<UUID> nonEncryptableUuidList;

    @EncryptableKey
    Map<UUID, String> encryptableUuidMap;

    Map<UUID, String> nonEncryptableUuidMap;
} 
```

Of course ID parameters passed on the URL should be converted manually.

```java
@GetMapping("/invoices/{customerId}")
public List<BigDecimal> getExpenses(@PathVariable String customerId) {
    UUID encryptedUserId = UUID.fromString(customerId);
    UUID userId = EncryptorsHolder.getUuidEncryptor().decrypt(encryptedUserId);
    List<BigDecimal> invoices = accountinService.getExpensesOfCustomer(userId);
    return invoices;
}
```

Don;t forget to init the `EncryptorsHolder` with a password:

```java
EncryptorsHolder.initEncryptorsWithPassword("abracadabra");
```

Unfortunately using a static factory was the only way to cope with the fact that de/serializers object are created by Jackson directly and cannot be initialized with a password (any suggestions?).

## Operation flows

The following procedures apply to both UUID and TSID.

The flow of **saving** data with its assigned UUID is:

1. The sequential UUID is created, added as primary key and saved along with the data it refers to

2. The sequential UUID is encrypted to avoid the guessing sequence attack

3. The encrypted UUID is then exported as string (it's faster and easier to encrypt the original data rather than its string representation - they have the same entropy)

4. The client saves the returned string for further references

The flow of **getting** the data is:

1. The client queries the system with the received reference string

2. The system transforms the string into an UUID (the encrypted one)

3. The encrypted UUID is decrypted into the actual UUID

4. The decrypted sequential UUID is searched on the database and the data is returned to the client

Every reference of the UUID must be encrypted before being sent and exported as string (i.e. when listing) so it could be useful to reserve a volatile (non persistable and definitely non indexed) field in the data for the string value representation of the encrypted UUID to be returned to clients (hint: use [Jackson](https://github.com/FasterXML/jackson) annotation to automate this).

## References

Refer to this blog post for further details.
