package fr.hamtec.locdvd;

import android.app.Activity;
import android.content.Intent;

public class MonActivity extends Activity {
    private  void startService(){
        Intent intent = new Intent( MonActivity.this, LocDVDIntentService.class);
        intent.putExtra( "waitDuration", 30_000 );
        startService( intent );
    }
}
