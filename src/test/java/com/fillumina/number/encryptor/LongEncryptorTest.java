package com.fillumina.number.encryptor;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongEncryptorTest extends AbstractLongEncryptorTest {

    public static void main(String[] args) {
        LongEncryptor ne = new LongEncryptor("barabba");

        for (long l=0; l<100; l++) {
            assertConvert(ne, l);
            System.out.println("" + l + "\t" + ne.encrypt(l));
        }
    }

    @Override
    LongEncryptor createEncryptor(String key) {
        return new LongEncryptor(key);
    }
}
