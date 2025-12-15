package br.edu.utfpr.ProjetoIDRAPI.utils;

import jakarta.persistence.Id;

import java.lang.reflect.Field;

public class EntityUtils {

    @SuppressWarnings("unchecked")
    public static <T, ID> ID getIdValue(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Class<?> entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    return (ID) field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access ID field", e);
                }
            }
        }

        throw new IllegalStateException("No field annotated with @Id found in class " + entityClass.getName());
    }

    public static <T> void setIdValue(T entity, Object value) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Class<?> entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    field.set(entity, value);
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access ID field", e);
                }
            }
        }

        throw new IllegalStateException("No field annotated with @Id found in class " + entityClass.getName());
    }
}
