package fr.hamtec.locdvd;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class LocDVDIntentService extends IntentService {
 
    public LocDVDIntentService( String name ) {
        super( name );
    }
    
    @Override
    protected void onHandleIntent( @Nullable Intent intent ) {
        int waitDuration = intent.getIntExtra( "waitDuration", 1000 );
        try {
            Thread.sleep( waitDuration );
        } catch ( InterruptedException ex ) {
            ex.printStackTrace( );
        }
    }
}
