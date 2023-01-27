package com.fillumina.keyencryptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Encrypt/decrypt all {@link Encryptable} annotated fields into an object tree.
 * The passed objects are encrypted/decrypted in place.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ObjectEncryptor {

    public static ObjectEncryptor create(String password) {
        return ObjectEncryptor.builder()
                .addEntry(Long.class, new LongEncryptor(password))
                .addEntry(UUID.class, new UuidEncryptor(password))
                .build();
    }

    public static ObjectEncryptor createCached(String password) {
        return ObjectEncryptor.builder()
                .addEntry(Long.class, new CachedEncryptor<>(new LongEncryptor(password)))
                .addEntry(UUID.class, new CachedEncryptor<>(new UuidEncryptor(password)))
                .build();
    }

    private final Map<Class<?>, Encryptor> encryptorMap;

    public ObjectEncryptor(Map<Class<?>, Encryptor> encryptorMap) {
        this.encryptorMap = encryptorMap;
    }

    /** Encrypts the parameter if it has a compatible encryptor. */
    public <T> T encrypt(T t) {
        Class<?> clazz = t.getClass();
        Encryptor encryptor = encryptorMap.get(clazz);
        if (encryptor != null) {
            return (T) encryptor.encrypt(t);
        }
        return t;
    }

    /** Decrypts the parameter if it has a compatible encryptor. */
    public <T> T decrypt(T t) {
        Class<?> clazz = t.getClass();
        Encryptor encryptor = encryptorMap.get(clazz);
        if (encryptor != null) {
            return (T) encryptor.decrypt(t);
        }
        return t;
    }

    /** Encrypt all fields annotated with {@link Encryptable} in place. */
    public void encryptTree(Object object) {
        parse(true, object);
    }

    /** Decrypt all fields annotated with {@link Encryptable} in place. */
    public void decryptTree(Object object) {
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

        public ObjectEncryptor build() {
            return new com.fillumina.keyencryptor.ObjectEncryptor(encryptorMap);
        }
    }

    public static ObjectEncryptor.Builder builder() {
        return new ObjectEncryptor.Builder();
    }

}
