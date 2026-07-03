package br.gov.pr.idr.infra.shared.storage;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class MinioStorageGateway implements StorageGateway {

    private final MinioClient client;
    private final MinioProperties properties;

    public MinioStorageGateway(final MinioClient client, final MinioProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @PostConstruct
    public void ensureBucketExists() {
        try {
            final var exists = client.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.bucketName())
                    .build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.bucketName())
                        .build());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao inicializar o bucket do MinIO: " + properties.bucketName(), e);
        }
    }

    @Override
    public void store(final String key, final String contentType, final byte[] content) {
        try (final InputStream stream = new ByteArrayInputStream(content)) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(key)
                    .stream(stream, (long) content.length, -1L)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao armazenar o arquivo no MinIO: " + key, e);
        }
    }

    @Override
    public byte[] retrieve(final String key) {
        try (final var response = client.getObject(GetObjectArgs.builder()
                .bucket(properties.bucketName())
                .object(key)
                .build())) {
            return response.readAllBytes();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw NotFoundException.with("Arquivo não encontrado no storage: " + key);
            }
            throw new IllegalStateException("Falha ao recuperar o arquivo do MinIO: " + key, e);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao recuperar o arquivo do MinIO: " + key, e);
        }
    }

    @Override
    public void delete(final String key) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(key)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao remover o arquivo do MinIO: " + key, e);
        }
    }
}
