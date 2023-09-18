package fr.hamtec.locdvd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements ListDVDFragment.OnDVDSelectedListener{
    String TAG = "HB";
    private ListView listDrawer;
    private String[] drawerItems;
    DrawerLayout drawerLayout;
    private static final String TAG_FRAGMENT_LISTDVD = "FragmentListDVD";
    
    /**
     *  This class was deprecated in API level 30.
     *  Use the standard java.util.concurrent or Kotlin concurrency utilities instead.
     */
    public class AsyncReadEmbeddedData extends AsyncTask <String, Integer, Boolean> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute( ) {
            //Drawable drawable = getResources().getDrawable( R.drawable.custom_progressdialog );
            progressDialog = new ProgressDialog( MainActivity.this );
            progressDialog.setTitle( R.string.initialisation_de_la_basse_de_donnees );
            //progressDialog.setProgressDrawable( drawable );
            progressDialog.setIndeterminate( true );
            //progressDialog.setMax( 15 );
            //progressDialog.setProgress( 1 );
            progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
            progressDialog.show();
        }
    
        @Override
        protected void onPostExecute( Boolean aBoolean ) {
            progressDialog.dismiss();
            
            FragmentManager fragmentManager = getSupportFragmentManager();
            ListDVDFragment listDVDFragment = (ListDVDFragment)fragmentManager.findFragmentByTag( TAG_FRAGMENT_LISTDVD );
    
            if ( listDVDFragment != null ){
                listDVDFragment.updateDVDList();
            }
        }
    
        int valeur;
        @Override
        protected void onProgressUpdate( Integer... values ) {
            progressDialog.setMessage( String.format( getString( R.string.x_dvd_inseres_dans_la_base ),values[0] ) );
            //progressDialog.incrementProgressBy(values[0]);
        }
    
        @Override
        protected Boolean doInBackground( String... params ) {
            //-compteur - suite
            boolean result = false;
            String dataFile = params[0];
            
            InputStream file = null;
            InputStreamReader reader = null;
            BufferedReader bufferedReader = null;
            try {
                //-compteur
                int counter = 0;
                
                file = getAssets().open( "data.txt" );
                reader =new InputStreamReader( file );
                bufferedReader = new BufferedReader( reader );
                String line = null;
                while ( ( line = bufferedReader.readLine( ) ) != null ){
                    String[] data = line.split( "\\|" );
                    if ( data != null && data.length == 4 ){
                        DVD dvd = new DVD();
                        dvd.setTitre( data[0] );
                        //static Integer | valueOf(String s) -> static Integer | decode(String nm)
                        dvd.setAnnee( Integer.decode(data[1]) );
                        dvd.setActeurs( data[2].split(",") );
                        dvd.setResume( data[3] );
                        dvd.insert( MainActivity.this );
                        //-compteur suite
                        publishProgress( ++counter );
                        try {
                            Thread.sleep( 1000 );
                        } catch ( InterruptedException ex ) {
                            ex.printStackTrace( );
                        }
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
                        // compteur - suite
                        result = true;
                    } catch ( IOException e ) {
                        e.printStackTrace( );
                    }
                }
            }
            return result;
        }
    }
    
    /**
     * Fermeture de Drawer
     */
    private  void closeDrawer(){
        drawerLayout.closeDrawer( GravityCompat.START );
    }
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        drawerLayout = findViewById( R.id.main_drawer );
        
        listDrawer = findViewById( R.id.main_DrawerList );
        drawerItems = getResources().getStringArray( R.array.drawer_Items );
        listDrawer.setAdapter( new ArrayAdapter<String>( this, R.layout.listitem_drawer, drawerItems ) );
        listDrawer.setOnItemClickListener( ( parent, view, position, id ) -> {
                switch ( position ){
                    case 0 -> {
                        Intent intent = new Intent(MainActivity.this,
                                MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        closeDrawer();
                    }
                    case 1 -> {
                        startActivity(new Intent(MainActivity.this, AddDVDActivity.class));
                        //closeDrawer();
                    }
                    case  2 -> {
                        SearchFragment searchFragment = new SearchFragment();
                        openDetailFragment( searchFragment );
                        closeDrawer();
                    }
                }
        } );
        // L'invocation de la méthode readEmbeddeData est conditionnée à l'abscence de la préférence utilisateur
        SharedPreferences sharedPreferences = getSharedPreferences("fr.hamtec.locDVD.prefs", Context.MODE_PRIVATE );
        if ( !sharedPreferences.getBoolean( "valeurPref", false ) ){
            readEmbbeddedData();
        }
    }
    private void openFragment( Fragment fragment, String tag ){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace( R.id.main_placeHolder, fragment,tag );
        transaction.addToBackStack( null );
        transaction.commit();
    }
    private void readEmbbeddedData(){
        
        AsyncReadEmbeddedData asyncReadEmbeddedData = new AsyncReadEmbeddedData();
        asyncReadEmbeddedData.execute( "data.txt" );
        
    }
    @Override
    protected void onResume( ) {
        super.onResume( );
        ListDVDFragment listDVDFragment = new ListDVDFragment();
        openFragment( listDVDFragment, TAG_FRAGMENT_LISTDVD );
    }
    private void openDetailFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
    
        if ( findViewById( R.id.detail_placeholder ) == null ){
            transaction.replace( R.id.main_placeHolder, fragment );
            Log.i( "HB", "version smartphone");
        }else {
            transaction.replace( R.id.detail_placeholder, fragment );
            Log.i( "HB", "version tablette");
        }
        transaction.addToBackStack( null );
        transaction.commit();
    }
    private void startViewDVDActivity( long dvdId){
        ViewDVDFragment viewDVDFragment = new ViewDVDFragment();
        Bundle bundle = new Bundle();
        bundle.putLong( "dvdId", dvdId );
        viewDVDFragment.setArguments( bundle );
        openDetailFragment( viewDVDFragment );
    }
    @Override
    public void onDVDSelected( long dvdId ) {
        startViewDVDActivity( dvdId );
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_principale, menu );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        switch ( item.getItemId() ){
            case R.id.menu_reinitialiser -> {
                ensureReInitializeApp();
                return true;
            }
            case R.id.menu_information -> {
                showInformations();
                return true;
            }
        }
        return super.onOptionsItemSelected( item );
    }
    public void ensureReInitializeApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( R.string.comfirme_reinitialisation_title );
        builder.setMessage( R.string.comfirme_reinitialisation_message );
        builder.setNegativeButton( R.string.non, null );
        builder.setPositiveButton( R.string.oui, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick( DialogInterface dialog, int which ) {
                LocalSQLiteOpenHelper.deleteDatabase( MainActivity.this );
                readEmbbeddedData();
            }
        } );
        AlertDialog dialog = builder.create( );
        dialog.show();
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
}