package legacy;

public class OldArchiveSystem {

    private boolean available = true;

    public int archive(String data, long archiveNumber) {
        if (!available) {
            return -2;
        }

        if (archiveNumber <= 0) {
            return -1;
        }

        System.out.println("Archived item " + archiveNumber);
        System.out.println(data);

        return 0;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}