package br.gov.pr.idr.infra.iam.user.models.auth;

public record AuthLoginResponse(String accessToken, String refreshToken) {}
