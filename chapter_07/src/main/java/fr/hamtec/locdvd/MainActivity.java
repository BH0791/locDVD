package fr.hamtec.locdvd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onResume( ) {
        super.onResume( );
        ListDVDFragment listDVDFragment = new ListDVDFragment();
        openFrament(listDVDFragment);
    }
    
    private void openFrament( Fragment fragment ) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        transaction.replace( R.id.main_placeHolder, fragment );
        transaction.addToBackStack( null );
        
        transaction.commit();
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        
        
        
        // L'invocation de la méthode readEmbeddeData est conditionnée à l'abscence de la préférence utilisateur
        SharedPreferences sharedPreferences = getSharedPreferences("fr.hamtec.locDVD.prefs", Context.MODE_PRIVATE );
        
        
        if ( !sharedPreferences.getBoolean( "embeddedDataInserted", false ) ){
            
            if ( sharedPreferences.contains( "enbeddedDataInsered" ) ){
                //- TODO
            }else {
                readEmbbeddedData();
            }
           
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
                    dvd.titre = data[0];
                    //static Integer | valueOf(String s) -> static Integer | decode(String nm)
                    dvd.annee = Integer.decode(data[1]);
                    dvd.acteurs = data[2].split(",");
                    dvd.resume = data[3];
                    dvd.insert( this );
                    
                }
                
            }
            
        }catch ( IOException e ){
            e.printStackTrace();
        }finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close( );
                    reader.close( );
                    //-ici
                    SharedPreferences sharedPreferences = getSharedPreferences( "fr.hamtec.locDVD.prefs", Context.MODE_PRIVATE );//??
                    SharedPreferences.Editor editor = sharedPreferences.edit( );
                    editor.putBoolean( "enbeddedDataInsered", true );
                    editor.apply( );
                } catch ( IOException e ) {
                    e.printStackTrace( );
                }
            }
        }
        
        
        
    }
    
    
    
    
    
    
}