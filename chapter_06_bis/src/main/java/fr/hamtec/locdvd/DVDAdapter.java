package fr.hamtec.locdvd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DVDAdapter extends ArrayAdapter<DVD> {
    
    Context context;
    
    public DVDAdapter( Context context, List < DVD > objects ) {
        super( context, -1, objects );
        this.context = context;
    }
    
    
    @SuppressLint( "InflateParams" )
    @NonNull
    @Override
    public View getView( int pos, View convertView, ViewGroup parent ) {
        
        View view;
        
        if ( convertView == null ){
            LayoutInflater layoutInflater = (LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = layoutInflater.inflate( R.layout.listitem_dvd, parent, false );
        }else {
            view = convertView;
        }
        
        DVD dvd = getItem( pos );
        //- La réf du dvd courant est stockée dans la vue
        view.setTag( dvd );
        
        TextView titre = view.findViewById( R.id.listItemDVD_titre );
        TextView annee = view.findViewById( R.id.listItemDVD_annee );
        TextView resume = view.findViewById( R.id.listItemDVD_resume );
        
        titre.setText( dvd.getTitre() );
        annee.setText( String.valueOf(dvd.getAnnee()) );
        resume.setText( dvd.getResume() );
        
        
        return view;
    }
}
