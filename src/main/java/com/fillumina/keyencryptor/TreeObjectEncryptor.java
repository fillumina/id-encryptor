package com.fillumina.keyencryptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Encrypt/decrypt all {@link Encryptable} annotated fields into an object tree.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class TreeObjectEncryptor {

    public static TreeObjectEncryptor create(String password) {
        return TreeObjectEncryptor.builder()
                .addEntry(Long.class, new LongEncryptor(password))
                .addEntry(UUID.class, new UuidEncryptor(password))
                .build();
    }

    private final Map<Class<?>, Encryptor> encryptorMap;

    public TreeObjectEncryptor(Map<Class<?>, Encryptor> encryptorMap) {
        this.encryptorMap = encryptorMap;
    }

    public void encryptObject(Object object) {
        parse(true, object);
    }

    public void decryptObject(Object object) {
        parse(false, object);
    }

    private void parse(boolean encrypt, Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            Encryptable encryptedAnnotation = field.getAnnotation(Encryptable.class);
            if (encryptedAnnotation != null) {
                try {
                    field.setAccessible(true);
                    Encryptor encryptor = encryptorMap.get(fieldClass);
                    Object fieldValue = field.get(object);
                    Object processed = encrypt
                            ? encryptor.encrypt(fieldValue)
                            : encryptor.decrypt(fieldValue);
                    field.set(object, processed);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (!fieldClass.getCanonicalName().startsWith("java.")) {
                try {
                    field.setAccessible(true);
                    parse(encrypt, field.get(object));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static class Builder {

        private Map<Class<?>,Encryptor> encryptorMap = new HashMap<>();

        public <T> Builder addEntry(final Class<T> clazz, Encryptor<T> encryptor) {
            this.encryptorMap.put(clazz, encryptor);
            return this;
        }

        public TreeObjectEncryptor build() {
            return new com.fillumina.keyencryptor.TreeObjectEncryptor(encryptorMap);
        }
    }

    public static TreeObjectEncryptor.Builder builder() {
        return new TreeObjectEncryptor.Builder();
    }

}
