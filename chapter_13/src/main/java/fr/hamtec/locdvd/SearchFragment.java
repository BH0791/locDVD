package fr.hamtec.locdvd;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.collection.LruCache;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment  extends Fragment {
    
    EditText searchText;
    Button searchButton;
    ListView searchList;
    String api_key="62d96ef75676fba47c537de195f1b3c6";
    
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    
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
    
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }
    
    private void launchSearch( ) {
        //-->
        try {
            String title = URLEncoder.encode( searchText.getText().toString(), "UTF-8" );
            String url = String.format( "https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&language=fr-FR", api_key, title );
            Log.d( "Recherche", "url :" + url );
            
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    jsonRequestListener,
                    errorListener
            );
            getRequestQueue().add( request );
            
        }catch ( UnsupportedEncodingException e ){
            Log.d( "Recherche", "erreur ; " + e.getMessage() );
            e.printStackTrace();
        }
    }
    
    private Response.Listener< JSONObject > jsonRequestListener = new Response.Listener <>( ) {
        @Override
        public void onResponse( JSONObject response ) {
            //-->
            try {
                
                ArrayList < Movie > listOfMovies = new ArrayList <>( );
                JSONArray jsonArray = response.getJSONArray( "results" );
                
                for ( int i = 0; i < jsonArray.length( ); i++ ) {
                    
                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                    
                    Movie movie = new Movie( );
                    movie.title = jsonObject.getString( "title" );
                    movie.releaseDate = jsonObject.getString( "release_date" );
                    movie.movieId = jsonObject.getString( "id" );
                    movie.overview = jsonObject.getString( "overview" );
                    
                    listOfMovies.add( movie );
                    
                    
                    
                }
                
                SearchListAdapter searchListAdapter = new SearchListAdapter( getActivity( ), listOfMovies );
                searchList.setAdapter( searchListAdapter );
                
            } catch ( JSONException e ) {
                Log.e( "JSON", e.getLocalizedMessage( ) );
            }
        }
    };
    
    private  Response.ErrorListener errorListener = new Response.ErrorListener( ) {
        @Override
        public void onErrorResponse( VolleyError error ) {
            //-->
            Log.d( "Recherche", "Erreur " + error.getMessage() );
        }
    };
    
    class  SearchListAdapter extends ArrayAdapter<Movie>{
        
        Context context;
        public SearchListAdapter( Context context, List<Movie> movies ){
            super(context, R.layout.listitem_movie, movies);
            this.context = context;
        }
        
        
        @Override
        public View getView( int pos, View convertView, ViewGroup parent ) {
            
            View view = null;
            
            if ( convertView == null ){
                LayoutInflater layoutInflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                // TODO ajouter parent et false a la place de null
                view = layoutInflater.inflate( R.layout.listitem_movie, parent, false );
            }else {
                view = convertView;
            }
            
            Movie movie = getItem( pos );
            view.setTag( movie );
            
            TextView titre = view.findViewById( R.id.movie_title );
            TextView dateSortie = view.findViewById( R.id.movie_releaseDate);
            final Button detailButton = view.findViewById( R.id.movie_detail );
            Button closeButton= view.findViewById(R.id.movie_closeDetail);
            
            final RelativeLayout detailLayout = view.findViewById(R.id.movie_detailLayout);
            final NetworkImageView detailPoster = view.findViewById(R.id.movie_poster);
            TextView detailPlot = view.findViewById(R.id.movie_plot);
            
            detailButton.setVisibility( View.VISIBLE );
            detailLayout.setVisibility( View.GONE );
            
            titre.setText( movie.title );
            dateSortie.setText( movie.releaseDate );
            
            detailButton.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick( View v ) {
                    detailLayout.setVisibility( View.VISIBLE );
                    detailButton.setVisibility( View.GONE );
                    
                    String url = String.format( "https://api.themoviedb.org/3/movie/%s?api_key=%s&language=fr-FR", movie.movieId, api_key );
                    
                    JsonObjectRequest jsonObjectRequest;
                    jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse( JSONObject response ) {
                            //--
                            try {
                                
                                String posterPath = response.getString( "poster_path" );
                                String plot = response.getString( "overview" );
                                detailPlot.setText( plot );
                                
                                String url = "https://image.tmdb.org/t/p/w500/" + posterPath;
                                detailPoster.setImageUrl(url, getImageLoader());
                                
                            }catch ( JSONException e ){
                                Log.e( "JSON", e.getLocalizedMessage() );
                            }
                        }
                    },
                            new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse( VolleyError error ) {
                                    Log.e( "DETAIL", error.getLocalizedMessage() );
                                }
                            }
                            );
                    
                    getRequestQueue().add( jsonObjectRequest );
                    
                }
            } );
            
            closeButton.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick( View v ) {
                    detailLayout.setVisibility( View.GONE );
                    detailButton.setVisibility( View.VISIBLE );
                }
            } );
            
            return view;
        }
    }
    
    ImageLoader getImageLoader()  {
        
        if(imageLoader==null) {
            ImageLoader.ImageCache imageCache =  new ImageLoader.ImageCache( ) {
                
                LruCache<String, Bitmap> cache = new LruCache<>( 10 );
                
                public Bitmap getBitmap( String url ) {
                    return cache.get( url );
                }
                
                @Override
                public void putBitmap( String url, Bitmap bitmap ) {
                    cache.put( url, bitmap );
                }
            };
            
            imageLoader = new ImageLoader(getRequestQueue(), imageCache);
            
        }
        
        return imageLoader;
    }
}
