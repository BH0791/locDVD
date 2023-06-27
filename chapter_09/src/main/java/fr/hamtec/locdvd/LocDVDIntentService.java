package fr.hamtec.locdvd;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import androidx.annotation.Nullable;

public class LocDVDIntentService extends IntentService {
    
    public LocDVDIntentService(  ) {
        super( "LocDVDIntentService" );
    }
    
    @Override
    protected void onHandleIntent( Intent intent ) {
        //Récupérer les données étendues de l'intention.
        //public int getIntExtra (String name, int defaultValue)
        //1000 la valeur à renvoyer si aucune valeur du type souhaité n'est stockée avec le nom donné.
        //waitDuration Le nom de l'élément souhaité.
        int waitDuration = intent.getIntExtra( "waitDurarion", 1000 );
        
        Messenger messenger = intent.getParcelableExtra( "messager" );
        
        try {
            Thread.sleep( waitDuration );
        }catch ( InterruptedException e ){
            e.printStackTrace();
        }
        
        if ( messenger != null ){
            Message message = Message.obtain();
            Bundle replyData = new Bundle();
            replyData.putString( "reply", "Le service est terminé" );
            message.setData( replyData );
        }
        
    }
}
