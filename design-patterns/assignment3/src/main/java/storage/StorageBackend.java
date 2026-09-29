package storage;

public interface StorageBackend {
    void save(int id, String data) throws StorageException;
}