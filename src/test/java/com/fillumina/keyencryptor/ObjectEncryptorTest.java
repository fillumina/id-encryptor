package com.fillumina.keyencryptor;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ObjectEncryptorTest {
    private static final Long NESTED_LONG_VALUE = 123L;
    private static final UUID NESTED_UUID_VALUE = UUID.randomUUID();
    private static final Long BEAN_LONG_VALUE = 123L;
    private static final UUID BEAN_UUID_VALUE = UUID.randomUUID();

    private static final String PASSWORD = "xyz123abc-/o";

    private ABean bean;
    private ObjectEncryptor objectEncryptor;

    @BeforeEach
    public void init() {
        ANestedBean nested = new ANestedBean(NESTED_LONG_VALUE, NESTED_UUID_VALUE);
        this.bean = new ABean(BEAN_LONG_VALUE, BEAN_UUID_VALUE, nested);

        this.objectEncryptor = ObjectEncryptor.create(PASSWORD);
    }

    @Test
    public void testUnparsedBeanValues() {
        assertThat(bean.getEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getNotEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);
        assertThat(bean.getNotEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);

        ANestedBean nested = bean.getNestedBean();

        assertThat(nested.getNestedEncryptableLong()).isEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedNotEncryptableLong()).isEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedEncryptableUUID()).isEqualTo(NESTED_UUID_VALUE);
        assertThat(nested.getNestedNotEncryptableUUID()).isEqualTo(NESTED_UUID_VALUE);
    }

    @Test
    public void shouldEncryptObject() {
        objectEncryptor.encryptTree(bean);

        assertThat(bean.getEncryptableLong()).isNotEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getNotEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getEncryptableUUID()).isNotEqualTo(BEAN_UUID_VALUE);
        assertThat(bean.getNotEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);

        ANestedBean nested = bean.getNestedBean();

        assertThat(nested.getNestedEncryptableLong()).isNotEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedNotEncryptableLong()).isEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedEncryptableUUID()).isNotEqualTo(NESTED_UUID_VALUE);
        assertThat(nested.getNestedNotEncryptableUUID()).isEqualTo(NESTED_UUID_VALUE);
    }

    @Test
    public void shouldDecryptObject() {
        objectEncryptor.encryptTree(bean);
        objectEncryptor.decryptTree(bean);

        testUnparsedBeanValues();
    }

    @Test
    public void shouldEncryptLong() {
        Long encrypted = objectEncryptor.encrypt(BEAN_LONG_VALUE);

        assertThat(encrypted)
                .isNotNull()
                .isNotEqualTo(BEAN_LONG_VALUE);
    }

    @Test
    public void shouldDecryptLong() {
        Long encrypted = objectEncryptor.encrypt(BEAN_LONG_VALUE);
        Long decrypted = objectEncryptor.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(BEAN_LONG_VALUE);
    }

    @Test
    public void shouldEncryptUUID() {
        UUID encrypted = objectEncryptor.encrypt(BEAN_UUID_VALUE);

        assertThat(encrypted)
                .isNotNull()
                .isNotEqualTo(BEAN_UUID_VALUE);
    }

    @Test
    public void shouldDecryptUUID() {
        UUID encrypted = objectEncryptor.encrypt(BEAN_UUID_VALUE);
        UUID decrypted = objectEncryptor.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(BEAN_UUID_VALUE);
    }

    public static class ANestedBean {

        @Encryptable
        private Long nestedEncryptableLong;

        private Long nestedNotEncryptableLong;

        @Encryptable
        private UUID nestedEncryptableUUID;

        private UUID nestedNotEncryptableUUID;

        public ANestedBean(Long longValue, UUID uuidValue) {
            this.nestedEncryptableLong = longValue;
            this.nestedNotEncryptableLong = longValue;
            this.nestedEncryptableUUID = uuidValue;
            this.nestedNotEncryptableUUID = uuidValue;
        }

        public Long getNestedEncryptableLong() {
            return nestedEncryptableLong;
        }

        public Long getNestedNotEncryptableLong() {
            return nestedNotEncryptableLong;
        }

        public UUID getNestedEncryptableUUID() {
            return nestedEncryptableUUID;
        }

        public UUID getNestedNotEncryptableUUID() {
            return nestedNotEncryptableUUID;
        }
    }

    public static class ABean {

        @Encryptable
        private Long encryptableLong;

        private Long notEncryptableLong;

        @Encryptable
        private UUID encryptableUUID;

        private UUID notEncryptableUUID;

        private ANestedBean nestedBean;

        public ABean(Long longValue, UUID uuidValue, ANestedBean nestedBean) {
            this.encryptableLong = longValue;
            this.notEncryptableLong = longValue;
            this.encryptableUUID = uuidValue;
            this.notEncryptableUUID = uuidValue;
            this.nestedBean = nestedBean;
        }

        public Long getEncryptableLong() {
            return encryptableLong;
        }

        public Long getNotEncryptableLong() {
            return notEncryptableLong;
        }

        public UUID getEncryptableUUID() {
            return encryptableUUID;
        }

        public UUID getNotEncryptableUUID() {
            return notEncryptableUUID;
        }

        public ANestedBean getNestedBean() {
            return nestedBean;
        }
    }
}
