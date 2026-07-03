package br.gov.pr.idr.domain.shared.storage;

public interface StorageGateway {

    void store(String key, String contentType, byte[] content);

    byte[] retrieve(String key);

    void delete(String key);
}
