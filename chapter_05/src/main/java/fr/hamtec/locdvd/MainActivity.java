package fr.hamtec.locdvd;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        SharedPreferences sharedPreferences = getSharedPreferences("fr.hamtec.locdvd.prefs", Context.MODE_PRIVATE );
        if ( !sharedPreferences.getBoolean( "valeurPref", false ) ){
            readEmbbeddedData();
        }
        
    }
    
    /**
     * Lire les données et les enregistrer
     */
    private void readEmbbeddedData(){
        
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        
        try {
            file = getAssets().open( "data.txt" );
            reader =new InputStreamReader( file );
            bufferedReader = new BufferedReader( reader );
            
            String line = null;
            
            while ( ( line = bufferedReader.readLine( ) ) != null ){
                
                String[] data = line.split( "\\|" );
                
                if ( data != null && data.length == 4 ){
                    DVD dvd = new DVD();
                    dvd.setTitre(data[0]);
                    dvd.setAnnee(Integer.decode(data[1]));
                    dvd.setActeurs(data[2].split(","));
                    dvd.setResume(data[3]);
                    
                    dvd.insert( this );
                    
                }
                
            }
            
        }catch ( IOException e ){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                reader.close();
                SharedPreferences sharedPreferences = getSharedPreferences("fr.hamtec.locDVD.prefs",Context.MODE_PRIVATE);//??
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("valeurPref",true);
                editor.commit();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        
        
        
    }
}