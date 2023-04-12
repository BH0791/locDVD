package fr.hamtec.locdvd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ViewDVDActivity extends AppCompatActivity {
    
    TextView txtTitreDVD;
    TextView txtAnneeDVD;
    TextView txtResumeFilm;
    LinearLayout layoutActeurs;
    TextView txtDateDernierVisionnage;
    Button setDateVisionnage;
    
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
        //---
        setDateVisionnage = findViewById( R.id.setDateVisionnage );
        txtDateDernierVisionnage = findViewById( R.id.dateVisionnage );
        
        Intent intent = getIntent();
        long dvdId = intent.getLongExtra( "dvdId", -1 );
        
        dvd = DVD.getDVD( this, dvdId );
        
        //---
        setDateVisionnage.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                
                showDatePicker();
                
            }
        } );
    
    }
    
    private void showDatePicker(){
        
        DatePickerDialog datePickerDialog;
        
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener( ) {
            @Override
            public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear, dayOfMonth );
                
                dvd.setDateVisionnage( calendar.getTimeInMillis() );
                dvd.update( ViewDVDActivity.this );
                
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd-MM-yyyy" );
                
                String dateValue = String.format( getString( R.string.date_dernier_visionnage_avec_valeur ), simpleDateFormat.format( calendar.getTime() ) );
                txtDateDernierVisionnage.setText( dateValue );
                
            }
        };
        
        Calendar calendar = Calendar.getInstance();
        
        if ( dvd.dateVisionnage > 0 ){
            calendar.setTimeInMillis( dvd.dateVisionnage );
        }
        
        datePickerDialog = new DatePickerDialog( this, onDateSetListener,
                calendar.get( Calendar.YEAR ),
                calendar.get( Calendar.MONTH ),
                calendar.get( Calendar.DAY_OF_MONTH )
        );
        
        datePickerDialog.show();
    
    }
    
    @Override
    protected void onResume() {
        super.onResume();

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
