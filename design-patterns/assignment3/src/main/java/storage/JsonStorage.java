package storage;

public class JsonStorage implements StorageBackend {

    @Override
    public void save(int id, String data) {
        System.out.println("Saved artifact " + id + " to JSON");
        System.out.println(data);
    }
}