package backend.database;

public class DBInitializer {
    private final InMemoryDB db;

    public DBInitializer(InMemoryDB db) {
        this.db = db;
    }

    public void seed() {
        db.SeedData();
    }
}
