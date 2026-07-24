package br.gov.pr.idr.infra.shared.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(String url, String accessKey, String secretKey, String bucketName) {}
