package br.gov.pr.idr.infra.iam.security.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CorsProperties")
class CorsPropertiesTest {

    @Test
    @DisplayName("deve retornar lista vazia quando origens não são informadas")
    void shouldReturnEmptyListWhenOriginsAreNull() {
        final var properties = new CorsProperties(null);

        assertTrue(properties.allowedOrigins().isEmpty());
    }

    @Test
    @DisplayName("deve remover espaços, barra final e valores vazios das origens")
    void shouldNormalizeOrigins() {
        final var properties = new CorsProperties(List.of(" https://idr.app.pb.utfpr.edu.br/ ", "", "http://localhost:5173"));

        assertEquals(List.of("https://idr.app.pb.utfpr.edu.br", "http://localhost:5173"), properties.allowedOrigins());
    }
}
