package fr.hamtec.locdvd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    
    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }
    
    
    private void launchSearch( ) {
        //-->
        try {
            
            String api_key = "62d96ef75676fba47c537de195f1b3c6";
            String titre = URLEncoder.encode( searchText.getText().toString(), "UTF-8" );
            String url = String.format( "https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&language=fr-FR", api_key, titre );
            
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    jsonRequestListener,
                    errorListener
            );
            getRequestQueue().add( request );
            
        }catch ( UnsupportedEncodingException e ){
            e.printStackTrace();
        }
    }
    
    private Response.Listener< JSONObject > jsonRequestListener = new Response.Listener < JSONObject >( ) {
        @Override
        public void onResponse( JSONObject response ) {
            //-->
        }
    };
    
    private  Response.ErrorListener errorListener = new Response.ErrorListener( ) {
        @Override
        public void onErrorResponse( VolleyError error ) {
            //-->
        }
    };
}
