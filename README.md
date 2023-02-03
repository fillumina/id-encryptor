# ID Encryptor

Encrypts and optionally serializes to and from JSON (using [Jackson](https://github.com/FasterXML/jackson) annotations) long and UUID fields in a scrambled form to avoid data leakage. 

## Scenario

A unit of data is addressed by an SQL Database using an identifier. It can be *natural* (being part of the data) or *surrogate* (assigned externally). A commonly used, and very practical, surrogate identifier is a sequential number called ID generally assigned by the database itself. This is very efficient on a single system but suffers the problem of not being unique in a distributed environment: every independent system (or at least independent database) has its own sequence. One solution is to use very large random numbers (with a very small collision probability) or to create them in a way that they are unique among the distributed system (in most cases the unicity constraint can be restricted into the context of the distributed application).

### UUID

An [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) is basically a random number of almost 128 bit (some bits are reserved) big enough so the probability of two values being created equal is *extremely* low. Unfortunately the same randomness that makes them unique behave very inefficiently with the way most databases organize their indexes, the B-Tree, leading to horrible performances. To overcome this pitfall a new type of UUID has been created, the **sequential UUID**, that is generated using a *node-id* and a *timestamp* in a monotonic sequence of 128 bit numbers. Sequential UUIDs work well with database indexes but pack into the value some info that could be harmful. Depending on the implementation it could be even possible to guess the next sequence value making UUID *predictable*. This might lead to data leakage (i.e. when exposed by a public API).

In case having a unique non guessable ID is a requirement **sequential numerical values can be encrypted**: that is the purpose of this package.

### TSID

[TSID](https://github.com/f4b6a3/tsid-creator) is a smaller version of UUID that is only 64 bits long. It has the same features of a full blown UUID with some constraints due to it having far less bits available. TSID can be encoded in a single long value and can be generated in a node and time dependent sequences (again to fix the index problem) so for the point of view of an application it's just like any other ID (just not starting from 0). Alas the TSID sequence can be guessed much more easily than that of UUID because of the much less randomness available. A TSID encoded long doesn't fit into the [maximum number that javascript can accept as integer](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER) and must be exported as a string if consumed by Javascript.

The project doesn't refer directly to TSID for generality (because they are effectively just longs) but offers plenty of ways to work with long identifiers.

## Exporting a Long ID as a UUID

It is perfectly legit to exported a sequential long ID encrypted  to make the next sequence value hard to guess (and it would work at the cost of having to export them as strings). 

It is also possible, and supported, to create an `UUID` combining the long ID with a fixed `nodeId` for each different node (and optionally a `fieldId` if it is important to have different UUIDs for the same value of two different entities). This is useful to adapt an application not designed to work in a distributed environment (i.e. to export data to a centralized application by several clients). It also makes an interesting option to keep the best performances from the local db while still be able to communicate in a distributed environment.

## Encryption

The proposed encryption algorithms are [Blowfish](https://en.wikipedia.org/wiki/Blowfish_(cipher)) for long (64 bit) and [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) for UUID (128 bit) used with the [ECB mode](https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm). ECB mode is discouraged for encrypting multi block messages but it's perfectly adequate if the data to encrypt matches the size of one single cipher block.

## Cache

All operations of encryption, decryption, serialization and deserialization are [memoized](https://en.wikipedia.org/wiki/Memoization) (cached) to improve efficiency and speed. The cache can be accessed concurrently and uses weak references so that cached memory could be reclaimed by the GC.

## Jackson serialization

The ID encryption should happen at the **API boundaries** and the *serialization* of data into [JSON](https://www.json.org/json-en.html) strings is a perfect place to encode them (away from the service layer that should know nothing about it). An added benefit is that all the annotated indexes will be automatically translated back and forth (and longs will be converted into strings as needed). The project provides serialization and deserialization helpers for both longs and UUIDs when used as single fields, into lists or as keys in a map. An optional *fieldId* parameter is provided in the annotation for encrypting longs (it's ignored for UUIDs) to allow a further scrambling so that equal values will not be encrypted in the same string (the same password is shared along all encryptable fields in the project).

There are annotations (those with `LongAsUuid` in the name) to transform a long ID into an UUID. This will require to add an optional `nodeId` in the factory and an optional `fieldId` in the annotation to distinguish the UUID values of the same ID on different entities where this might be needed (so a *Client* with ID=2 will not have the same UUID of an *Invoice* with ID=2 which might be problematic for security reasons).

This is an example of a class to serialize with annotate fields, it should be quite self-explanatory:

```java
public static class Bean {
    // serialized as an encrypted string
    @Encryptable
    Long encryptableLongValue = 1;

    // uses a seed (2) to encrypt 1 as a different string than the previous
    @Encryptable(2)
    Long encryptableLongValue2 = 1;

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

Of course ID **parameters passed on the URL** should be converted manually.

```java
@GetMapping("/invoices/{customerId}")
public List<Invoice> getInvoices(@PathVariable String customerId) {
    UUID userId = EncryptorsHolder.decryptUuid(customerId);
    return accountinService.getInvoicesOfCustomer(userId);
}
```

Don't forget to **initialize** the `EncryptorsHolder`: there are several init methods available with different options.

```java
EncryptorsHolder.initEncryptorsWithPassword("abracadabra");
```

## References

Refer to this blog post for further details.
