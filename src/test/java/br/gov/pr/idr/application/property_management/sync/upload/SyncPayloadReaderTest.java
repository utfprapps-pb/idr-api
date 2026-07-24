package br.gov.pr.idr.application.property_management.sync.upload;

import br.gov.pr.idr.domain.property_management.sync.payload.SyncPayloadReader;
import br.gov.pr.idr.domain.shared.tactical.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SyncPayloadReader")
class SyncPayloadReaderTest {

    @Test
    @DisplayName("deve lançar NullPointerException quando data é nulo")
    void shouldThrowWhenDataIsNull() {
        assertThrows(NullPointerException.class, () -> SyncPayloadReader.of(null));
    }

    @Test
    @DisplayName("uuid: deve retornar null quando chave ausente")
    void uuidShouldReturnNullWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertNull(reader.uuid("id"));
    }

    @Test
    @DisplayName("uuid: deve retornar o próprio valor quando já é UUID")
    void uuidShouldReturnSameInstanceWhenAlreadyUuid() {
        final var id = UUID.randomUUID();
        final var reader = SyncPayloadReader.of(Map.of("id", id));

        assertEquals(id, reader.uuid("id"));
    }

    @Test
    @DisplayName("uuid: deve converter string para UUID")
    void uuidShouldParseFromString() {
        final var id = UUID.randomUUID();
        final var reader = SyncPayloadReader.of(Map.of("id", id.toString()));

        assertEquals(id, reader.uuid("id"));
    }

    @Test
    @DisplayName("bigDecimal: deve retornar ZERO quando chave ausente")
    void bigDecimalShouldReturnZeroWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertEquals(BigDecimal.ZERO, reader.bigDecimal("price"));
    }

    @Test
    @DisplayName("bigDecimal: deve retornar o próprio valor quando já é BigDecimal")
    void bigDecimalShouldReturnSameInstanceWhenAlreadyBigDecimal() {
        final var value = new BigDecimal("12.50");
        final var reader = SyncPayloadReader.of(Map.of("price", value));

        assertEquals(value, reader.bigDecimal("price"));
    }

    @Test
    @DisplayName("bigDecimal: deve converter Number genérico")
    void bigDecimalShouldConvertFromNumber() {
        final var reader = SyncPayloadReader.of(Map.of("price", 10));

        assertEquals(BigDecimal.valueOf(10.0), reader.bigDecimal("price"));
    }

    @Test
    @DisplayName("bigDecimal: deve converter string")
    void bigDecimalShouldParseFromString() {
        final var reader = SyncPayloadReader.of(Map.of("price", "7.25"));

        assertEquals(new BigDecimal("7.25"), reader.bigDecimal("price"));
    }

    @Test
    @DisplayName("doubleValue: deve retornar 0.0 quando chave ausente")
    void doubleValueShouldReturnZeroWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertEquals(0.0, reader.doubleValue("area"));
    }

    @Test
    @DisplayName("doubleValue: deve retornar o próprio valor quando já é Double")
    void doubleValueShouldReturnSameInstanceWhenAlreadyDouble() {
        final var reader = SyncPayloadReader.of(Map.of("area", 3.5));

        assertEquals(3.5, reader.doubleValue("area"));
    }

    @Test
    @DisplayName("doubleValue: deve converter Number genérico")
    void doubleValueShouldConvertFromNumber() {
        final var reader = SyncPayloadReader.of(Map.of("area", 4));

        assertEquals(4.0, reader.doubleValue("area"));
    }

    @Test
    @DisplayName("doubleValue: deve converter string")
    void doubleValueShouldParseFromString() {
        final var reader = SyncPayloadReader.of(Map.of("area", "2.75"));

        assertEquals(2.75, reader.doubleValue("area"));
    }

    @Test
    @DisplayName("requiredString: deve retornar valor quando presente")
    void requiredStringShouldReturnValueWhenPresent() {
        final var reader = SyncPayloadReader.of(Map.of("name", "Fazenda"));

        assertEquals("Fazenda", reader.requiredString("name"));
    }

    @Test
    @DisplayName("requiredString: deve lançar quando ausente")
    void requiredStringShouldThrowWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertThrows(UnprocessableEntityException.class, () -> reader.requiredString("name"));
    }

    @Test
    @DisplayName("requiredString: deve lançar quando em branco")
    void requiredStringShouldThrowWhenBlank() {
        final var reader = SyncPayloadReader.of(Map.of("name", "   "));

        assertThrows(UnprocessableEntityException.class, () -> reader.requiredString("name"));
    }

    @Test
    @DisplayName("stringList: deve retornar lista vazia quando chave ausente")
    void stringListShouldReturnEmptyWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertTrue(reader.stringList("technicianIds").isEmpty());
    }

    @Test
    @DisplayName("stringList: deve converter itens da lista para string")
    void stringListShouldConvertItemsToString() {
        final var id = UUID.randomUUID();
        final var reader = SyncPayloadReader.of(Map.of("technicianIds", List.of(id)));

        assertEquals(List.of(id.toString()), reader.stringList("technicianIds"));
    }

    @Test
    @DisplayName("stringList: deve lançar quando valor não é uma lista")
    void stringListShouldThrowWhenNotAList() {
        final var reader = SyncPayloadReader.of(Map.of("technicianIds", "not-a-list"));

        assertThrows(UnprocessableEntityException.class, () -> reader.stringList("technicianIds"));
    }

    @Test
    @DisplayName("mapList: deve retornar lista vazia quando chave ausente")
    void mapListShouldReturnEmptyWhenAbsent() {
        final var reader = SyncPayloadReader.of(Map.of());

        assertTrue(reader.mapList("collaborators").isEmpty());
    }

    @Test
    @DisplayName("mapList: deve retornar lista de mapas quando itens são objetos")
    void mapListShouldReturnItemsWhenObjects() {
        final var item = Map.<String, Object>of("name", "João");
        final var reader = SyncPayloadReader.of(Map.of("collaborators", List.of(item)));

        assertEquals(List.of(item), reader.mapList("collaborators"));
    }

    @Test
    @DisplayName("mapList: deve lançar quando valor não é uma lista")
    void mapListShouldThrowWhenNotAList() {
        final var reader = SyncPayloadReader.of(Map.of("collaborators", "not-a-list"));

        assertThrows(UnprocessableEntityException.class, () -> reader.mapList("collaborators"));
    }

    @Test
    @DisplayName("mapList: deve lançar quando item da lista não é um objeto")
    void mapListShouldThrowWhenItemIsNotAMap() {
        final var reader = SyncPayloadReader.of(Map.of("collaborators", List.of("not-a-map")));

        assertThrows(UnprocessableEntityException.class, () -> reader.mapList("collaborators"));
    }

    @Test
    @DisplayName("stringValue: deve retornar valor convertido para string")
    void stringValueShouldReturnValueAsString() {
        final var map = Map.<String, Object>of("fileName", "foto.png");

        assertEquals("foto.png", SyncPayloadReader.stringValue(map, "fileName"));
    }

    @Test
    @DisplayName("stringValue: deve retornar null quando chave ausente")
    void stringValueShouldReturnNullWhenAbsent() {
        final Map<String, Object> map = Map.of();

        assertNull(SyncPayloadReader.stringValue(map, "fileName"));
    }

    @Test
    @DisplayName("base64Value: deve decodificar string base64 válida")
    void base64ValueShouldDecodeValidBase64() {
        final var content = "conteudo".getBytes();
        final var map = Map.<String, Object>of("base64", Base64.getEncoder().encodeToString(content));

        assertArrayEquals(content, SyncPayloadReader.base64Value(map, "base64"));
    }

    @Test
    @DisplayName("base64Value: deve retornar array vazio quando chave ausente")
    void base64ValueShouldReturnEmptyArrayWhenAbsent() {
        final Map<String, Object> map = Map.of();

        assertArrayEquals(new byte[0], SyncPayloadReader.base64Value(map, "base64"));
    }

    @Test
    @DisplayName("base64Value: deve retornar array vazio quando valor em branco")
    void base64ValueShouldReturnEmptyArrayWhenBlank() {
        final var map = Map.<String, Object>of("base64", "   ");

        assertArrayEquals(new byte[0], SyncPayloadReader.base64Value(map, "base64"));
    }
}
