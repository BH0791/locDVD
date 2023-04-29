package fr.hamtec.locdvd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewDVDActivity extends AppCompatActivity {
    TextView txtTitreDVD;
    TextView txtAnneeDVD;
    TextView txtResumeFilm;
    LinearLayout layoutActeurs;
    DVD dvd;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        
        //- Affectation du fichier de layout
        setContentView(R.layout.activity_viewdvd);

        //-Obtention des références sur les composants
        txtTitreDVD = findViewById(R.id.titreDVD);
        txtAnneeDVD = findViewById(R.id.anneeDVD);
        txtResumeFilm = findViewById(R.id.resumeFilm);
        layoutActeurs = findViewById( R.id.layoutActeurs );
        
        Intent intent = getIntent();
        long dvdId = intent.getLongExtra( "dvdId", -1 );
        
        
        dvd = DVD.getDVD( this, dvdId );
    
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i( "HAMID", "==> 2: " + dvd.getTitre());
        txtTitreDVD.setText(dvd.getTitre());
        txtAnneeDVD.setText(String.format(getString(R.string.annee_de_sortie), dvd.getAnnee()));
        
        for ( String acteur : dvd.getActeurs() ){
            TextView textView = new TextView( this );
            textView.setText( acteur );
            layoutActeurs.addView( textView );
        }
        
        txtResumeFilm.setText(dvd.getResume());

    }
}
