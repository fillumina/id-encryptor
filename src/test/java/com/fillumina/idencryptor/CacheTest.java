package com.fillumina.idencryptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CacheTest {

    @Test
    public void testGetNoCache() {
        Cache<String,String> noCache = Cache.getNoCache();

        String response1 = noCache.getOrCreate("one", () -> "1");
        assertEquals("1", response1);

        String response2 = noCache.getOrCreate("one", () -> "2");
        assertEquals("2", response2);
    }

}
