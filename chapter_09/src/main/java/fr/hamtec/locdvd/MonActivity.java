package fr.hamtec.locdvd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import androidx.annotation.NonNull;

import java.util.logging.Handler;

public class MonActivity extends Activity {
    private  void startService(){
        
//        Handler handler = new android.os.Handler(  ){
//            @Override
//            public void handleMessage( Message msg ) {
//                Bundle reply = msg.getData();
//                String message = reply.getString( "reply" );
//            }
//        };
        
//        Messenger messenger = new Messenger( ( handler );
        
        Intent intent = new Intent( MonActivity.this, LocDVDIntentService.class);
        intent.putExtra( "waitDuration", 30_000 );
        startService( intent );
    }
}
