package backend.database;

public class DBInitializer {
    private final IDatabase db;

    public DBInitializer(IDatabase db) {
        this.db = db;
    }

    public void seed() {
        db.SeedData();
    }
}
