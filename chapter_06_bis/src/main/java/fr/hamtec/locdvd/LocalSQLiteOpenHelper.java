package fr.hamtec.locdvd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSQLiteOpenHelper extends SQLiteOpenHelper {
    
    private static final String DB_NAME = "LocDVD-d.db";
    private static final int DB_VERSION = 1;
    
    public LocalSQLiteOpenHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }
    
    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sqlFilTable = "CREATE TABLE DVD(id INTEGER PRIMARY KEY, titre TEXT, annee NUMERIC, acteurs TEXT, resume TEXT);";
        db.execSQL( sqlFilTable );
    }
    
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        // TODO onUpgrade a écrire
    }
}
