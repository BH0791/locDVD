package fr.hamtec.locdvd;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment  extends Fragment {
    
    public static class Movie{
        
        public String titre;
        public String releaseDate;
        public String movieId;
        public String overview;
        
    }
    
    class  SearchListAdapter extends ArrayAdapter<Movie>{
        
        Context context;
        public SearchListAdapter( Context context, List<Movie> movies ){
            super(context, R.layout.listitem_movie, movies);
        }
        
        
        @Override
        public View getView( int pos, View convertView, ViewGroup parent ) {
            
            View view = null;
            
            if ( convertView == null ){
                LayoutInflater layoutInflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                view = layoutInflater.inflate( R.layout.listitem_movie, null );
            }else {
                view = convertView;
            }
            
            Movie movie = getItem( pos );
            view.setTag( movie );
            
            TextView titre = view.findViewById( R.id.movie_titre );
            TextView dateSortie = view.findViewById( R.id.movie_releaseDate);
            Button detailButton = view.findViewById( R.id.movie_detail );
            
            titre.setText( movie.titre );
            dateSortie.setText( movie.releaseDate );
            
            return view;
        }
    }
    
    private EditText searchText;
    private Button searchButton;
    private ListView searchList;
    
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState ) {
        
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_search, null );
        
        searchText = view.findViewById( R.id.search_queryText );
        searchButton = view.findViewById( R.id.search_queryLaunch );
        searchList = view.findViewById( R.id.search_List );
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
            try {
                
                ArrayList<Movie> listOfMovies = new ArrayList<>();
                JSONArray jsonArray = response.getJSONArray( "results" );
                
                for ( int i = 0; i < jsonArray.length( ); i++ ) {
                    
                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                    Movie movie = new Movie();
                    movie.titre = jsonObject.getString( "titre" );
                    movie.releaseDate = jsonObject.getString( "release_date" );
                    movie.movieId = jsonObject.getString( "id" );
                    movie.overview  = jsonObject.getString( "overview" );
                    listOfMovies.add( movie );
                    
                }
                
                SearchListAdapter searchListAdapter = new SearchListAdapter( getActivity(), listOfMovies );
                searchList.setAdapter( searchListAdapter );
                
            }catch ( JSONException e ){
                Log.e( "JSON", e.getLocalizedMessage());
            }
        }
    };
    
    private  Response.ErrorListener errorListener = new Response.ErrorListener( ) {
        @Override
        public void onErrorResponse( VolleyError error ) {
            //-->
        }
    };
}
