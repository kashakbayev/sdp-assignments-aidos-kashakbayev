package storage;

public class DatabaseStorage implements StorageBackend {

    @Override
    public void save(int id, String data) {
        System.out.println("Saved artifact " + id + " to database");
        System.out.println(data);
    }
}