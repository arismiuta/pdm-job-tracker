package app.unijobs.ro.unijobs_android.database;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Maniga on 1/13/2018.
 */

public class Database {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
