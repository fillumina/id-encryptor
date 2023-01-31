package com.fillumina.keyencryptor;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class UUIDUtil {

    /**
     * Build a UUID from two long values. It's worth noting that only 62 bits of
     * the {@code leastSigBits} can be used.
     * <p>
     * The {@link UUID#UUID(long,long) } constructor just use all the passed bits disregarding type
     * and variant information and can be used if preserving compatibility is not required.
     *
     * @see <a href='https://github.com/cowtowncoder/java-uuid-generator'>java-uuid-generator</a>
     * @see UUID#getLeastSignificantBits()
     * @see UUID#getMostSignificantBits()
     *
     * @return
     */
    public static UUID constructUUID(long mostSigBits, long leastSigBits) {
        // first, ensure type is ok
        mostSigBits &= ~0xF000L; // remove high nibble of 6th byte
        // l1 |= (long) (type.raw() << 12);
        // second, ensure variant is properly set too (8th byte; most-sig byte of second long)
        leastSigBits = ((leastSigBits << 2) >>> 2); // remove 2 MSB
        leastSigBits |= (2L << 62); // set 2 MSB to '10'
        return new UUID(mostSigBits, leastSigBits);
    }
}
