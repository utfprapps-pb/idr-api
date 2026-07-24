package br.gov.pr.idr.domain.property_management.sync.payload;

import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class SyncPayloadReader {

    private final Map<String, Object> data;

    private SyncPayloadReader(final Map<String, Object> data) {
        this.data = Objects.requireNonNull(data, "data não pode ser nulo");
    }

    public static SyncPayloadReader of(final Map<String, Object> data) {
        return new SyncPayloadReader(data);
    }

    public UUID uuid(final String key) {
        final var value = data.get(key);
        if (value == null) return null;
        if (value instanceof UUID uuid) return uuid;
        return UUID.fromString(value.toString());
    }

    public BigDecimal bigDecimal(final String key) {
        final var value = data.get(key);
        return switch (value) {
            case null -> BigDecimal.ZERO;
            case BigDecimal bd -> bd;
            case Number n -> BigDecimal.valueOf(n.doubleValue());
            default -> new BigDecimal(value.toString());
        };
    }

    public Double doubleValue(final String key) {
        final var value = data.get(key);
        return switch (value) {
            case null -> 0.0;
            case Double d -> d;
            case Number n -> n.doubleValue();
            default -> Double.parseDouble(value.toString());
        };
    }

    public String requiredString(final String key) {
        final var value = data.get(key);
        if (value == null || value.toString().isBlank()) {
            throw new UnprocessableEntityException("Campo obrigatório ausente ou vazio: " + key);
        }
        return value.toString();
    }

    public List<String> stringList(final String key) {
        final var value = data.getOrDefault(key, List.of());
        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }
        throw new UnprocessableEntityException("Campo deveria ser uma lista: " + key);
    }

    public List<Map<String, Object>> mapList(final String key) {
        final var value = data.getOrDefault(key, List.of());
        if (value instanceof List<?> list) {
            return list.stream().map(SyncPayloadReader::asMap).toList();
        }
        throw new UnprocessableEntityException("Campo deveria ser uma lista: " + key);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(final Object item) {
        if (item instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new UnprocessableEntityException("Item da lista deveria ser um objeto");
    }

    public static String stringValue(final Map<String, Object> map, final String key) {
        final var value = map.get(key);
        return value == null ? null : value.toString();
    }

    public static byte[] base64Value(final Map<String, Object> map, final String key) {
        final var value = stringValue(map, key);
        if (value == null || value.isBlank()) return new byte[0];
        return Base64.getDecoder().decode(value);
    }
}
