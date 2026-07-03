package br.gov.pr.idr.infra.shared.storage;

import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import okhttp3.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MinioStorageGateway")
class MinioStorageGatewayTest {

    @Mock MinioClient client;
    private MinioStorageGateway gateway;

    @BeforeEach
    void setUp() {
        final var properties = new MinioProperties("http://localhost:9000", "key", "secret", "test-bucket");
        gateway = new MinioStorageGateway(client, properties);
    }

    @Test
    @DisplayName("deve criar o bucket quando ele não existe")
    void shouldCreateBucketWhenMissing() throws Exception {
        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        gateway.ensureBucketExists();

        verify(client).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    @DisplayName("não deve recriar o bucket quando ele já existe")
    void shouldNotRecreateBucketWhenExists() throws Exception {
        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        gateway.ensureBucketExists();

        verify(client, never()).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    @DisplayName("deve armazenar o arquivo via putObject")
    void shouldStoreFile() throws Exception {
        gateway.store("key.pdf", "application/pdf", new byte[]{1, 2, 3});

        verify(client).putObject(any(PutObjectArgs.class));
    }

    @Test
    @DisplayName("deve lançar IllegalStateException quando o armazenamento falha")
    void shouldThrowWhenStoreFails() throws Exception {
        doThrow(new IOException("boom")).when(client).putObject(any(PutObjectArgs.class));

        assertThrows(IllegalStateException.class, () -> gateway.store("key.pdf", "application/pdf", new byte[]{1}));
    }

    @Test
    @DisplayName("deve recuperar o conteúdo armazenado")
    void shouldRetrieveFile() throws Exception {
        final var response = new GetObjectResponse(new Headers.Builder().build(), "test-bucket", "us-east-1",
                "key.pdf", new ByteArrayInputStream(new byte[]{9, 8, 7}));
        when(client.getObject(any(GetObjectArgs.class))).thenReturn(response);

        final var content = gateway.retrieve("key.pdf");

        assertArrayEquals(new byte[]{9, 8, 7}, content);
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando o objeto não existe (NoSuchKey)")
    void shouldThrowNotFoundWhenKeyMissing() throws Exception {
        final var errorResponse = new ErrorResponse(
                "NoSuchKey", "not found", "test-bucket", "key.pdf", null, null, null);
        when(client.getObject(any(GetObjectArgs.class)))
                .thenThrow(new ErrorResponseException(errorResponse, null, "test-bucket"));

        assertThrows(NotFoundException.class, () -> gateway.retrieve("key.pdf"));
    }

    @Test
    @DisplayName("deve lançar IllegalStateException para outras falhas de recuperação")
    void shouldThrowIllegalStateForOtherRetrieveFailures() throws Exception {
        when(client.getObject(any(GetObjectArgs.class))).thenThrow(new IOException("boom"));

        assertThrows(IllegalStateException.class, () -> gateway.retrieve("key.pdf"));
    }

    @Test
    @DisplayName("deve remover o arquivo via removeObject")
    void shouldDeleteFile() throws Exception {
        gateway.delete("key.pdf");

        verify(client).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    @DisplayName("deve lançar IllegalStateException quando a remoção falha")
    void shouldThrowWhenDeleteFails() throws Exception {
        doThrow(new IOException("boom")).when(client).removeObject(any(RemoveObjectArgs.class));

        assertThrows(IllegalStateException.class, () -> gateway.delete("key.pdf"));
    }
}
