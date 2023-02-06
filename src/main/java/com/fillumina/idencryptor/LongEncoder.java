package com.fillumina.idencryptor;

/**
 * Encodes a <i>node-id</i> and an <i>ID</i> into a long value.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncoder {
    private final long nodeId;
    private final int nodeBits;
    private final int maxBit;

    public LongEncoder(int nodeBits, long nodeId) {
        this(nodeBits, nodeId, false);
    }

    /**
     * @see <a href='https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER'>
     * Mozilla Developer: Javascript MAX_SAFE_INTEGER</a>
     *
     * @param nodeBits bit reserved for node. Id will have 64 - nodeBits bit.
     * @param nodeId initialize the node identifier value.
     * @param javascriptCompliance limits the generated long to 52 bit to allow the number to be
     *                             used by javascript clients.
     */
    public LongEncoder(int nodeBits, long nodeId, boolean javascriptCompliance) {
        if (nodeBits > 32) {
            throw new IllegalArgumentException("not leaving enough bits for ID values");
        }
        this.maxBit = javascriptCompliance ? 52 : 64;
        this.nodeId = mask(nodeBits, nodeId) << (maxBit - nodeBits);
        this.nodeBits = nodeBits;
    }

    /** Upper limit of the value (the max actual value is {@code maxValue() - 1} */
    public long maxValue() {
        return 1L << (maxBit - nodeBits);
    }

    /** Upper limit of the nodeId (the max actual value is {@code maxNodeId() - 1} */
    public long maxNodeId() {
        return 1L << nodeBits;
    }

    public long getNodeId() {
        return nodeId;
    }

    public int getNodeBits() {
        return nodeBits;
    }

    public long encode(long id) {
        return mask(maxBit - nodeBits, id) | nodeId;
    }

    public long extractNodeIdFromEncodedValue(long encoded) {
        long nodeId = encoded >> (maxBit - nodeBits);
        // here nodeId is interpreted signed while we want it positive
        return nodeId >= 0 ? nodeId : (1 << nodeBits) + nodeId;
    }

    public long decode(long encoded) {
        return encoded & ~(createBitMask(nodeBits) << (maxBit - nodeBits));
    }

    static long mask(int bitNumber, long value) {
        long result = value & createBitMask(bitNumber);
        if (result != value) {
            throw new IllegalArgumentException("masked value " + result +
                    " different from given " + value);
        }
        return result;
    }

    static long createBitMask(int bitNumber) {
        if (bitNumber == 64) {
            return Long.MAX_VALUE;
        }
        if (bitNumber < 0) {
            throw new IllegalArgumentException("argument must be a positive integer, was " +
                    bitNumber);
        }
        return (1L << bitNumber) - 1;
    }
}
