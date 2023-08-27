package fr.hamtec.locdvd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.function.BinaryOperator;

public class DVD {
    
    long id;
    String titre;
    int annee;
    String[] acteurs;
    String resume;
    long dateVisionnage;
    
    public DVD(  ) {

    }

    private DVD(Cursor cursor){

        id = cursor.getLong(0);
        titre = cursor.getString(1);
        annee = cursor.getInt(2);
        acteurs = cursor.getString(3).split(";");
        resume = cursor.getString(4);
        dateVisionnage = cursor.getLong( 5 );

    }
    
    public void setDateVisionnage( long dateVisionnage ) {
        this.dateVisionnage = dateVisionnage;
    }
    
    public long getDateVisionnage( ) {
        return dateVisionnage;
    }
    
    /**
     * Méthode qui va fournir la liste de tous les DVD enregistrés par l'utilisateur
     * orderBy = "titre" cause problème => a réglé
     * @param context
     * @return
     */
    public static ArrayList<DVD> getDVDList(Context context){
        
        
        ArrayList<DVD> listDVD = new ArrayList<>();
        
        //  getReadableDatabase() permet d'obtenir une référence sur la BD ouverte en lecture.
        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        
        Cursor cursor = db.query(
                true,                                                                       // true si vous souhaitez que chaque ligne soit unique, false dans le cas contraire.
                "DVD",                                                                          // nom de la table sur laquelle compiler la requête.
                new String[]{"id", "titre", "annee", "acteurs", "resume", "dateVisionnage"},    // liste des colonnes à renvoyer
                null,                                                                           // filtre déclarant les lignes à renvoyer, mis en forme Clause SQL WHERE
                null,                                                                           // Vous pouvez inclure des ?s dans la sélection, qui sera remplacés par les valeurs de selectionArgs
                null,                                                                           // Un filtre déclarant comment regrouper des lignes, au format SQL Clause GROUP BY
                null,                                                                           // Un filtre déclare les groupes de lignes à inclure dans le curseur, si le regroupement de lignes est utilisé, formaté en tant que SQL HAVING
                "titre",                                                                           // Comment ordonner les lignes, au format en tant que clause SQL ORDER BY
                null                                                                            //Limite le nombre de lignes renvoyées par la requête, formaté en tant que clause LIMIT
                );

        while (cursor.moveToNext()){
            listDVD.add(new DVD(cursor));
        }

        cursor.close();
        db.close();

        return listDVD;
    }
    
    /**
     *  Permet d'obtenir un DVD à partir de son identifiant de base de donnée
     * @param context
     * @param id
     * @return
     */
    public static DVD getDVD(Context context, long id){
        DVD dvd = null;
        //id += 1;    //-Cause bug avec id=0
        
        LocalSQLiteOpenHelper helper = new LocalSQLiteOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        
        // String.valueOf(id) peut être remplacé par id régle java avec +
        String where = "id = " + String.valueOf(id);

        Cursor cursor = db.query(
                true,
                "DVD",
                new String[]{"id", "titre", "annee", "acteurs", "resume", "dateVisionnage"},
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
        values.put( "dateVisionnage", this.dateVisionnage );

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
