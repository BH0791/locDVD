package fr.hamtec.locdvd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment  extends Fragment {
    
    private EditText searchText;
    private Button searchButton;
    
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState ) {
        
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_search, null );
        
        searchText = view.findViewById( R.id.search_queryText );
        searchButton = view.findViewById( R.id.search_queryLaunch );
        searchButton.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                launchSearch();
            }
        } );
        
        return view;
    }
    
    private void launchSearch( ) {
    
    }
}
