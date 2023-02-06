package com.fillumina.idencryptor;

import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class Util {

    /**
     * Build a valid free form version 8 UUID from two long values. Only 62 bits of
     * the {@code leastSigBits} can be used and the {@code mostSigBits} will have its 12 to 16
     * bits overwritten by the type (set to 8, free form).
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
        mostSigBits |= (long) (8 << 12); // set type 8 (free form)
        // second, ensure variant is properly set too (8th byte; most-sig byte of second long)
        leastSigBits = ((leastSigBits << 2) >>> 2); // remove 2 MSB
        leastSigBits |= (2L << 62); // set 2 MSB to '10'
        return new UUID(mostSigBits, leastSigBits);
    }

}
