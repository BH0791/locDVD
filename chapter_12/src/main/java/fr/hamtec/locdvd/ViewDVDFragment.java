package fr.hamtec.locdvd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class ViewDVDFragment extends Fragment {
    TextView txtTitreDVD;
    TextView txtAnneeDVD;
    TextView txtResumeFilm;
    TextView txtDateDernierVisionnage;
    LinearLayout layoutActeurs;
    Button setDateVisionnage;
    Button setNotificacion;
    DVD dvd;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup contenair, Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        
        //- Affectation du fichier de layout
        View view = inflater.inflate(R.layout.activity_viewdvd, null);
        
        //-Obtention des références sur les composants
        txtTitreDVD = view.findViewById(R.id.titreDVD);
        txtAnneeDVD = view.findViewById(R.id.anneeDVD);
        txtResumeFilm = view.findViewById(R.id.resumeFilm);
        txtResumeFilm = view.findViewById(R.id.resumeFilm);
        layoutActeurs = view.findViewById( R.id.layoutActeurs );
        
        setDateVisionnage = view.findViewById( R.id.setDateVisionnage );
        setNotificacion = view.findViewById( R.id.setNotificacion );
        txtDateDernierVisionnage = view.findViewById( R.id.dateVisionnage );
        
        setNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        
        long dvdId = getArguments().getLong( "dvdId", -1 );
        dvd = DVD.getDVD( getActivity(), dvdId );
        
        setDateVisionnage.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                
                showDatePicker();
                
            }
        } );
        
        return view;
    }
    
    @SuppressLint( "NotificationPermission" )
    private void sendNotification( ) {
        
        Notification.Builder builder = new Notification.Builder( getActivity() );
        builder.setContentTitle(dvd.getTitre());
        builder.setContentText( dvd.getResume() );
        builder.setSmallIcon( R.drawable.ic_notification );
        
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getActivity(), 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        
        Notification notification;
        
        if( Build.VERSION.SDK_INT<16) {
            
            notification = builder.getNotification();
            
        }else{
            
            notification = builder.build();
            
            NotificationManager notificationManager = (NotificationManager ) getActivity().getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( (int)dvd.id, notification );
            
        }
        
        
        
        
    }
    
    private void showDatePicker( ) {
        
        DatePickerDialog datePickerDialog;
        
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener( ) {
            @Override
            public void onDateSet( DatePicker view, int year, int month, int dayOfMonth ) {
                Calendar calendar = Calendar.getInstance();
                calendar.set( year,dayOfMonth, dayOfMonth );
                
                dvd.setDateVisionnage( calendar.getTimeInMillis() );
                dvd.update( getActivity() );
                
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                
                String dateValue = String.format( getString( R.string.date_dernier_visionnage_avec_valeur ), simpleDateFormat.format( calendar.getTime() ) );
                txtDateDernierVisionnage.setText( dateValue );
            }
        };
        
        Calendar calendar = Calendar.getInstance();
        
        if ( dvd.dateVisionnage>0 ){
            calendar.setTimeInMillis( dvd.dateVisionnage );
        }
        
        datePickerDialog = new DatePickerDialog( getActivity(), onDateSetListener, calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ) );
        
        datePickerDialog.show();
    
    }
    
    @Override
    public void onResume() {
        super.onResume();
        txtTitreDVD.setText(dvd.getTitre());
        txtAnneeDVD.setText(String.format(getString(R.string.annee_de_sortie), dvd.getAnnee()));
        
        for ( String acteur : dvd.getActeurs() ){
            TextView textView = new TextView( getActivity() );
            textView.setText( acteur );
            layoutActeurs.addView( textView );
        }
        
        txtResumeFilm.setText(dvd.getResume());

    }
}
