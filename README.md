# ID Encryptor

Encrypt long values (64 bit) and UUIDs (128 bit).

## UUID

[UUID]([Universally unique identifier - Wikipedia](https://en.wikipedia.org/wiki/Universally_unique_identifier))s are a way to assign an Universally Unique Identifier to an object. An UUID is basically a random number of almost 128 bit (some bits are reserved) big enough so the probability of two values being created equals is *extremely* low (for any practical use they are *guaranteed unique*). Unfortunately the same randomness that makes them unique is really messing with the way most databases organize their indexes: the B-Tree. To overcome this pitfall a new version (several new versions indeed) of UUID have been created so to be generated as an increasing sequence of values. This new versions work well with indexes but make UUID predictable once one element of the sequence is known. In case this is a problem sequential UUIDs must be scrambled with another level of encryption. That is the purpose of this package.

## TSID

There is a smaller version of UUID that is only 64 bits long (against the 128 of the standard UUID) and It's called [TSID]([GitHub - f4b6a3/tsid-creator: A Java library for generating Time-Sorted Unique Identifiers (TSID).](https://github.com/f4b6a3/tsid-creator)). It has the same features of a full blown UUID but it's targeted towards smaller systems with a maximum node count of about 1024. Because the 64 bit TSID can be encoded in a single long value and its generated sequentially (again to fix the index problem) there is a long encryptor available in this package in case it must be scrambled.

## Encryption

The proposed encrypting algorithms are Blowfish for TSID (64 bit) and AES for UUID (128 bit) used with the [ECB mode]([The difference in five modes in the AES encryption algorithm - Highgo Software Inc.](https://www.highgo.ca/2019/08/08/the-difference-in-five-modes-in-the-aes-encryption-algorithm)). The ECB mode is discouraged to encrypt long messages but it's perfectly adequate if the data to encrypt matches the size of the cipher block.

## Cache

Cached version of ecryptors are also provided to improve performances. They use weak references so their memory could be reclaimed by the GC if needed.

## Operation flows

The following procedures apply to both UUID and TSID.

The flow of **saving** data with its assigned UUID is:

1. The UUID is created, added to and saved with the data it refers to

2. The created UUID is encrypted to avoid guessing sequence attack

3. The encrypted UUID is then exported as string (it's faster and easier to encrypt the original data rather than its string representation, they have the same entropy)

4. The client saves the returned string for further references

The flow of **getting** the data is:

1. The client queries the system with the received reference string

2. The system transforms the string into an UUID (the encrypted one)

3. The encrypted UUID is decrypted to the actual UUID

4. The decrypted UUID is searched on the database and the data is returned to the client

Every reference of the UUID must follow the saving procedure before being sent and exported as strings (i.e. when listing) so it could be useful to reserve a volatile (non persistable and definitely non indexed) field in the data for the string value representation of the encrypted UUID to be returned to clients (use Jackson annotation to automate this).

Because of the universality of the UUID all operations can be performed directly in the data object using local methods (no need for a dedicated service).

## References

Please refers to this blog post for furher details.




