package br.gov.pr.idr.infra.iam.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long expirationMs, long refreshTokenExpirationDays) {}
