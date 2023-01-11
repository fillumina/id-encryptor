/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.fillumina.keyencryptor;

import com.fillumina.keyencryptor.ConcurrentCache;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ConcurrentCacheTest {

    private ConcurrentCache<Integer, String> cache;

    @BeforeEach
    private void init() {
        this.cache = new ConcurrentCache<>(32, HashMap::new);
    }

    @Test
    public void shouldPutAndGet() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        cache.put(11, "eleven");
        cache.put(12, "twelve");
        cache.put(13, "therteen");

        cache.put(111, "hundred-eleven");
        cache.put(112, "hundred-twelve");
        cache.put(113, "hundred-therteen");

        assertEquals("one", cache.get(1));
        assertEquals("two", cache.get(2));
        assertEquals("three", cache.get(3));

        assertEquals("eleven", cache.get(11));
        assertEquals("twelve", cache.get(12));
        assertEquals("therteen", cache.get(13));

        assertEquals("hundred-eleven", cache.get(111));
        assertEquals("hundred-twelve", cache.get(112));
        assertEquals("hundred-therteen", cache.get(113));
    }
}
