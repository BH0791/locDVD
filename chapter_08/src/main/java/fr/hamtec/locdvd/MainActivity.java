package fr.hamtec.locdvd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements ListDVDFragment.OnDVDSelectedListener {
    @Override
    protected void onResume( ) {
        super.onResume( );
        ListDVDFragment listDVDFragment = new ListDVDFragment();
        openFrament(listDVDFragment);
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_principal, menu );
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        
        switch (item.getItemId()) {
            case R.id.menu_reinitialiser:
                // l'entrée Réinitialiser la base a été sélectionnée => boîtes de dialogue
                return true;
                
            case R.id.menu_informations:
                // l'entrée Informations a été sélectionnée => boîtes de dialogue
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
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
        
        ListView listDrawer = findViewById( R.id.main_drawerList );
        String [] drawerItems = getResources().getStringArray( R.array.drawer_Items );
        listDrawer.setAdapter( new ArrayAdapter<String>( this, R.layout.listitem_drawer, drawerItems  ) );
        
        listDrawer.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView < ? > parent, View view, int pos, long id ) {
                
                if(pos==0) {
                    Intent intent = new Intent(MainActivity.this,
                            MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if(pos==1)
                    startActivity(new Intent(MainActivity.this,
                            AddDVDActivity.class));
                
            
            }
        } );
        
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
    
    private  void openDetailFragment(Fragment fragment){
        
        Log.i( "HAMID", "méthode-openDetailFragment");
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        if ( findViewById( R.id.detail_placeholder ) == null ){
            transaction.replace( R.id.main_placeHolder, fragment );
            Log.i( "HAMID", "version smartphone");
        }else {
            transaction.replace( R.id.detail_placeholder, fragment );
            Log.i( "HAMID", "version tablette");
        }
        
        transaction.addToBackStack( null );
        transaction.commit();
        
    }
    
    @Override
    public void onDVDSelected( long dvdId ) {
        startViewDVDActivity( dvdId );
        // TODO onDVDSelected
        
    }
    
    private void startViewDVDActivity( long dvdId){
        Log.i( "HAMID", "méthode-startViewDVDActivity " + dvdId);
        ViewDVDFragment viewDVDFragment = new ViewDVDFragment();
        Bundle bundle = new Bundle();
        bundle.putLong( "dvdId", dvdId );
        viewDVDFragment.setArguments( bundle );
        openDetailFragment( viewDVDFragment );
        
    }
}