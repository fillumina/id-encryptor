# ID Encryptor

Encrypts long values (64 bit) and UUIDs (128 bit).

## Scenario

A unit of data (record) is addressed by an SQL Database using an identifier. An Identifier might be *natural* (being part of the data) or *surrogate* (assigned externally). A commonly used surrogate identifier is a number taken out of a sequence (a sort of serial number) called ID. This method is simple, compact and fast but once a system needs to talk with other systems this assigned ID is not unique anymore. Furthermore it's very dangerous to use an ID outside the scope of the database that creates it (IDs changes for various reasons and should not be exported other than as a temporary reference). One solution to the problem was to use very large random numbers: they can be assigned by any system autonomously and are guaranteed unique (under some conditions).

### UUID

[UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier)s are a way to assign an Universally Unique Identifier to an object. An UUID is basically a random number of almost 128 bit (some bits are reserved) big enough so the probability of two values being created equals is *extremely* low (under some conditions and for any practical use they are *guaranteed unique*). Unfortunately the same randomness that makes them unique works very badly with the way most databases organize their indexes, the B-Tree, leading to horrible performances. To overcome this pitfall a new type of UUID have been created, the sequential UUID, that generates them as an increasing sequence of 128 bit numbers. Sequential UUIDs work well with database indexes but make UUID predictable once one element of the sequence is known. In case this is a problem sequential UUIDs must be scrambled with another level of encryption: that is the purpose of this package.

### TSID

[TSID](https://github.com/f4b6a3/tsid-creator) is a smaller version of UUID that is only 64 bits long (against the 128 of the standard UUID). It has the same features of a full blown UUID but it's targeted towards smaller systems with a maximum node count of about 1024 (can be adjusted a bit depending on a key generation rate trade-off). Because the 64 bit TSID can be encoded in a single long value and is generated sequentially (again to fix the index problem) there is a long encryptor available in this package in case it needs to be scrambled.

## Encryption

The proposed encrypting algorithms are [Blowfish](https://en.wikipedia.org/wiki/Blowfish_(cipher)) for TSID (64 bit) and [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) for UUID (128 bit) used with the [ECB mode](https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm). The ECB mode is discouraged to encrypt long messages but it's perfectly adequate if the data to encrypt matches the size of the cipher block.

## Cache

Cached version of the encryptors are also provided to improve performances. They use weak references so their memory could be reclaimed by the GC.

## Operation flows

The following procedures apply to both UUID and TSID.

The flow of **saving** data with its assigned UUID is:

1. The sequential UUID is created, added as index and saved with the data it refers to

2. The sequential UUID is encrypted to avoid the guessing sequence attack

3. The encrypted UUID is then exported as string (it's faster and easier to encrypt the original data rather than its string representation - they have the same entropy)

4. The client saves the returned string for further references

The flow of **getting** the data is:

1. The client queries the system with the received reference string

2. The system transforms the string into an UUID (the encrypted one)

3. The encrypted UUID is decrypted into the actual UUID

4. The decrypted sequential UUID is searched on the database and the data is returned to the client

Every reference of the UUID must be encrypted before being sent and exported as strings (i.e. when listing) so it could be useful to reserve a volatile (non persistable and definitely non indexed) field in the data for the string value representation of the encrypted UUID to be returned to clients (use [Jackson](https://github.com/FasterXML/jackson) annotation to automate this).

Because of the universality of the UUID all operations can be performed directly in the data object using local methods (no need for a dedicated service/helper).

## References

Please refers to this blog post for furher details.
