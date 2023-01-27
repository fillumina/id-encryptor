package com.fillumina.keyencryptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        assertThatBeanHasNotBeenEncrypted();
    }

    @Test
    public void shouldEncryptObject() {
        objectEncryptor.encryptTree(bean);

        assertThatBeanHasBeenEncrypted(bean);
    }

    @Test
    public void shouldDecryptObject() {
        objectEncryptor.encryptTree(bean);
        objectEncryptor.decryptTree(bean);

        testUnparsedBeanValues();
    }

    @Test
    public void shouldEncryptIntoList() {
        long item1LongValue = 987L;
        UUID item1UuidValue = UUID.randomUUID();
        bean.addToList(new ANestedBean(item1LongValue, item1UuidValue));

        long item2LongValue = 543L;
        UUID item2UuidValue = UUID.randomUUID();
        bean.addToList(new ANestedBean(item2LongValue, item2UuidValue));

        objectEncryptor.encryptTree(bean);

        assertThatNestedBeanHasBeenEncrypted((ANestedBean) bean.getList().get(0), item1LongValue, item1UuidValue);
        assertThatNestedBeanHasBeenEncrypted((ANestedBean) bean.getList().get(1), item2LongValue, item2UuidValue);
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

    private void assertThatBeanHasNotBeenEncrypted() {
        assertThat(bean.getEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getNotEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);
        assertThat(bean.getNotEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);

        ANestedBean nested = bean.getNestedBean();

        assertThatNestedBeanHasNotBeenEncrypted(nested);
    }

    private void assertThatNestedBeanHasNotBeenEncrypted(ANestedBean nested) {
        assertThat(nested.getNestedEncryptableLong()).isEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedNotEncryptableLong()).isEqualTo(NESTED_LONG_VALUE);
        assertThat(nested.getNestedEncryptableUUID()).isEqualTo(NESTED_UUID_VALUE);
        assertThat(nested.getNestedNotEncryptableUUID()).isEqualTo(NESTED_UUID_VALUE);
    }

    private void assertThatBeanHasBeenEncrypted(ABean bean) {
        assertThat(bean.getEncryptableLong()).isNotEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getNotEncryptableLong()).isEqualTo(BEAN_LONG_VALUE);
        assertThat(bean.getEncryptableUUID()).isNotEqualTo(BEAN_UUID_VALUE);
        assertThat(bean.getNotEncryptableUUID()).isEqualTo(BEAN_UUID_VALUE);

        ANestedBean nested = bean.getNestedBean();

        assertThatNestedBeanHasBeenEncrypted(nested);
    }

    private void assertThatNestedBeanHasBeenEncrypted(ANestedBean nested) {
        assertThatNestedBeanHasBeenEncrypted(nested, NESTED_LONG_VALUE, NESTED_UUID_VALUE);
    }

    private void assertThatNestedBeanHasBeenEncrypted(ANestedBean nested, Long longValue, UUID uuidValue) {
        assertThat(nested.getNestedEncryptableLong()).isNotEqualTo(longValue);
        assertThat(nested.getNestedNotEncryptableLong()).isEqualTo(longValue);
        assertThat(nested.getNestedEncryptableUUID()).isNotEqualTo(uuidValue);
        assertThat(nested.getNestedNotEncryptableUUID()).isEqualTo(uuidValue);
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

        private List<Object> list = new ArrayList<>();

        private Map<Object, Object> map = new HashMap<>();

        public ABean(Long longValue, UUID uuidValue, ANestedBean nestedBean) {
            this.encryptableLong = longValue;
            this.notEncryptableLong = longValue;
            this.encryptableUUID = uuidValue;
            this.notEncryptableUUID = uuidValue;
            this.nestedBean = nestedBean;
        }

        public ABean addToList(ANestedBean item) {
            this.list.add(item);
            return this;
        }

        public ABean putToMap(final ANestedBean key, final ANestedBean value) {
            this.map.put(key, value);
            return this;
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

        public List<Object> getList() {
            return list;
        }

        public Map<Object, Object> getMap() {
            return map;
        }

        // tricky case: equals and hashCode depends on a modifiable key!

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.encryptableUUID);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ABean other = (ABean) obj;
            return Objects.equals(this.encryptableUUID, other.encryptableUUID);
        }

    }
}
