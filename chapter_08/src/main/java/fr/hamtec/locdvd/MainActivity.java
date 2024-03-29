package fr.hamtec.locdvd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements ListDVDFragment.OnDVDSelectedListener {
    
    DrawerLayout drawerLayout;
    
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
                ensureReInitializeApp();
                return true;
                
            case R.id.menu_informations:
                // l'entrée Informations a été sélectionnée => boîtes de dialogue
                showInformations();
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
        
        drawerLayout = findViewById( R.id.main_Drawer );
        
        ListView listDrawer = findViewById( R.id.main_DrawerList );
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
                    startActivity(new Intent(MainActivity.this, AddDVDActivity.class));
                    drawerLayout.closeDrawer( GravityCompat.START );
            
            }
        } );
        
        // L'invocation de la méthode readEmbeddeData est conditionnée à l'abscence de la préférence utilisateur
        SharedPreferences sharedPreferences = getSharedPreferences("fr.hamtec.locDVD.prefs", Context.MODE_PRIVATE );
        if ( !sharedPreferences.getBoolean( "valeurPref", false ) ){
            readEmbbeddedData();
        }
        
    }
    
    private void showInformations(){
        
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( R.string.infos );
        builder.setPositiveButton( R.string.fermer, null );
        
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate( R.layout.dialog_informations, null );
        
        TextView message = view.findViewById( R.id.dialog_message );
        message.setText( R.string.informations_message );
        message.setMovementMethod( new android.text.method.ScrollingMovementMethod() );
        
        builder.setView( view );
        builder.create().show();
        
    }
    
    private void ensureReInitializeApp(){
        
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( R.string.confirmer_reinitialisation_titre );
        builder.setMessage( R.string.confirmer_reinitilisation_message );
        
        builder.setNegativeButton( R.string.non, null );
        builder.setPositiveButton( R.string.oui, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick( DialogInterface dialog, int which ) {
                
                LocalSQLiteOpenHelper.deleteDatabase( MainActivity.this );
                readEmbbeddedData();
                
                Intent intent =new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                
                startActivity( intent );
                
            }
        } );
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        
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
                    editor.putBoolean( "valeurPref", true );
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
        
        if ( findViewById( R.id.detail_placeHolder ) == null ){
            transaction.replace( R.id.main_placeHolder, fragment );
            Log.i( "HAMID", "version smartphone");
        }else {
            transaction.replace( R.id.detail_placeHolder, fragment );
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