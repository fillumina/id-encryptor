package com.fillumina.keyencryptor;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * Creates a simple Concurrent Cache that uses a provided map implementation.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ConcurrentCache<K,V> implements Cache<K, V> {

    private final int mask;
    private final Map<K,V>[] mapArray;

    /**
     * Creates a concurrent {@link WeakHashMap} with a concurrency level set to 32.
     * @param concurrency
     */
    public ConcurrentCache() {
        this(32);
    }

    /**
     * Creates a concurrent {@link WeakHashMap}.
     * @param concurrency
     */
    public ConcurrentCache(int concurrency) {
        this(concurrency, WeakHashMap::new);
    }

    /**
     * @param concurrency the level of desired concurrency (how many concurrent threads can access
     * the map on average without contention)
     * @param supplier a {@code Map<K,V>} supplier. It shouldn't be synchronized as it will be
     * wrapped into a {@link Collections#synchronizedMap(java.util.Map)}.
     */
    public ConcurrentCache(int concurrency, Supplier<Map<K,V>> supplier) {
        int capacity = tableSizeFor(concurrency);
        this.mask = capacity / 2 - 1;
        this.mapArray = new Map[capacity];
        for (int i=0; i<capacity; i++) {
            Map<K,V> nodeMap = supplier.get();
            this.mapArray[i] = nodeMap;
        }
    }

    private Map<K,V> selectMap(K key) {
        int index = mask + hash(key) % mask;
        return mapArray[index];
    }

    @Override
    public V get(K key) {
        final Map<K, V> map = selectMap(key);
        synchronized(map) {
            return map.get(key);
        }
    }

    @Override
    public V put(K key, V value) {
        final Map<K, V> map = selectMap(key);
        synchronized(map) {
            return map.put(key,value);
        }
    }

    /**
     * @see java.util.HashMap#hash(java.lang.Object)
     *
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * @see java.util.HashMap#tableSizeFor(int)
     *
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : n + 1;
    }
}
