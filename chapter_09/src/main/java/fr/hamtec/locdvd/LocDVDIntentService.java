package fr.hamtec.locdvd;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class LocDVDIntentService extends IntentService {
    
    public LocDVDIntentService(  ) {
        super( "LocDVDIntentService" );
    }
    
    @Override
    protected void onHandleIntent( @Nullable Intent intent ) {
        
        int waitDuration = intent.getIntExtra( "waitDurarion", 1000 );
        
        try {
            Thread.sleep( waitDuration );
        }catch ( InterruptedException e ){
            e.printStackTrace();
        }
        
    }
}
