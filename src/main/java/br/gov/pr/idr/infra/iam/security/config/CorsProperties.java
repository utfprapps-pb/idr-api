package br.gov.pr.idr.infra.iam.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(List<String> allowedOrigins) {

    public CorsProperties {
        if (allowedOrigins == null) {
            allowedOrigins = List.of();
        }

        allowedOrigins = allowedOrigins.stream()
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .map(CorsProperties::removeTrailingSlash)
                .toList();
    }

    private static String removeTrailingSlash(String origin) {
        if (origin.endsWith("/")) {
            return origin.substring(0, origin.length() - 1);
        }

        return origin;
    }
}
