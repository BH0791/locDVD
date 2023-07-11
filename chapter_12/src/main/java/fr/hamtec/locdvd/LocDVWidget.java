package fr.hamtec.locdvd;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

public class LocDVWidget extends AppWidgetProvider {
    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {
        super.onUpdate( context, appWidgetManager, appWidgetIds );
        
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget_layout );
        ArrayList<DVD> list = DVD.getDVDList( context );
        
        Intent intent = new Intent( context, LocDVWidget.class );
        intent.setAction( AppWidgetManager.ACTION_APPWIDGET_UPDATE );
        intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS , appWidgetIds );
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        remoteViews.setOnClickPendingIntent( R.id.widget_title, pendingIntent );
        remoteViews.setOnClickPendingIntent( R.id.widget_summary, pendingIntent );
        
        for( int widgetId : appWidgetIds ){
            
            Random random = new Random();
            int randomIndex = random.nextInt( list.size() );
            
            DVD selected = list.get( randomIndex );
            
            remoteViews.setTextViewText( R.id.widget_title , selected.getTitre() );
            remoteViews.setTextViewText( R.id.widget_summary,  selected.getResume() );
            
            appWidgetManager.updateAppWidget( widgetId, remoteViews );
            
        
        }
        
    }
}
