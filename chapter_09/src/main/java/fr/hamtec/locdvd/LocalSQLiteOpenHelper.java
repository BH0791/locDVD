package fr.hamtec.locdvd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSQLiteOpenHelper extends SQLiteOpenHelper {
    
    private static final String DB_NAME = "LocDVD-v2.db";
    private static final int DB_VERSION = 2;
    
    public LocalSQLiteOpenHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }
    
    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sqlFilTable = "CREATE TABLE DVD(id INTEGER PRIMARY KEY, titre TEXT, annee NUMERIC, acteurs TEXT, resume TEXT, dateVisionnage NUMERIC);";
        db.execSQL( sqlFilTable );
    }
    
    /**
     * Ajouter un champs date de visionnage
     * @param db base
     */
    private void upgradeToVersion2(SQLiteDatabase db){
        
        String sqlCommand = "ALTER TABLE DVD ADD COLUMN dateVisionnage NUMERIC";
        db.execSQL( sqlCommand );
        
    }
    
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        for ( int i = oldVersion; i<newVersion; i++ ){
            int versionToUpdate = i+1;
            
            if ( versionToUpdate == 2 ){
                upgradeToVersion2( db );
            }else if ( versionToUpdate == 3 ){
                // Code pour la version 3
            }
        }
    }
    
    public static void deleteDatabase(Context context){
        
        context.deleteDatabase( DB_NAME );
        
    }
}
