package fr.hamtec.locdvd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DVD {
    
    long id;
    String titre;
    int annee;
    String[] acteurs;
    String resume;
    
    public DVD(  ) {

    }

    private DVD(Cursor cursor){

        id = cursor.getLong(0);
        titre = cursor.getString(1);
        annee = cursor.getInt(2);
        acteurs = cursor.getString(3).split(";");
        resume = cursor.getString(4);


    }
    
    
//-okey
    public static ArrayList<DVD> getDVDList(Context context){

        ArrayList<DVD> listDVD = new ArrayList<>();
        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                true,
                "DVD",
                new String[]{"id", "titre", "annee", "acteurs", "resume"},
                null,
                null,
                null,
                null,
                "titre",
                null
                );

        while (cursor.moveToNext()){
            listDVD.add(new DVD(cursor));
        }

        cursor.close();
        db.close();

        return listDVD;
    }
//-okey
    public static DVD getDVD(Context context, long id){

        DVD dvd = null;

        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String where = "id = " + String.valueOf(id);

        Cursor cursor = db.query(
                true,
                "DVD",
                new String[]{"id", "titre", "annee", "acteurs", "resume"},
                where,
                null,
                null,
                null,
                "titre",
                null
        );

        if (cursor.moveToFirst()){
            dvd = new DVD(cursor);
        }

        cursor.close();
        db.close();

        return dvd;
    }
//-okey
    public void insert(Context context){

        ContentValues values = new ContentValues();

        values.put("titre", this.titre);
        values.put("annee", this.annee);

        if (this.acteurs != null){
            String listActeurs = new String();

            for (int i = 0; i < this.acteurs.length; i++) {
                listActeurs += this.acteurs[i];

                if (i<this.acteurs.length - 1){
                    listActeurs += ";";
                }

            }
            values.put("acteurs", listActeurs);
        }

        values.put("resume", this.resume);

        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        this.id = db.insert("DVD", null, values);

        db.close();

    }

    public void update(Context context){

        ContentValues values = new ContentValues();

        values.put("titre", this.titre);
        values.put("annee", this.annee);

        if (this.acteurs != null){
            String listActeurs = new String();

            for (int i = 0; i < this.acteurs.length; i++) {
                listActeurs += this.acteurs;

                if (i<this.acteurs.length - 1){
                    listActeurs += ";";
                }

            }
            values.put("acteurs", listActeurs);
        }

        values.put("resume", this.resume);

        String whereClause = "id=" + String.valueOf(this.id);
        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.update("DVD", values, whereClause, null);

        db.close();

    }

    public void delete(Context context){

        String whereClause = "id= ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = String.valueOf(this.id);

        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete("DVD", whereClause, whereArgs);
        db.close();

    }

    public long getId( ) {
        return id;
    }
    
    public void setId( long id ) {
        this.id = id;
    }
    
    public String getTitre( ) {
        return titre;
    }
    
    public void setTitre( String titre ) {
        this.titre = titre;
    }
    
    public int getAnnee( ) {
        return annee;
    }
    
    public void setAnnee( int annee ) {
        this.annee = annee;
    }
    
    public String[] getActeurs( ) {
        return acteurs;
    }
    
    public void setActeurs( String[] acteurs ) {
        this.acteurs = acteurs;
    }
    
    public String getResume( ) {
        return resume;
    }
    
    public void setResume( String resume ) {
        this.resume = resume;
    }
}
