package fr.hamtec.locdvd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListDVDFragment  extends Fragment {
    
    public interface OnDVDSelectedListener{
        
        void onDVDSelected(long dvdId);
        // [...]
        
    }
    
    OnDVDSelectedListener onDVDSelectedListener;
    private ListView list;
    
    @Override
    public void onAttach( @NotNull Context activity ) {
        super.onAttach( activity );
        // try/catch
        onDVDSelectedListener = (OnDVDSelectedListener ) activity;
    }
    
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView( @NonNull @NotNull LayoutInflater inflater,
                              @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState ) {
        
        View view = inflater.inflate( R.layout.fragment_listdvd, null );
        list = view.findViewById( R.id.main_list );
        
        list.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView < ? > parent, View view, int position, long id ) {
                
                //startViewDVDActivity( id );
                
                if ( onDVDSelectedListener !=null ){
                    DVD selectedDvd = (DVD ) view.getTag();
                    onDVDSelectedListener.onDVDSelected( selectedDvd.id );
                }
            }
        } );
        
        return view;
    }
    
    @Override
    public void onResume( ) {
        super.onResume( );
        ArrayList <DVD> dvdList = DVD.getDVDList( getActivity() );
        
        DVDAdapter dvdAdapter = new DVDAdapter( getActivity(), dvdList );
        list.setAdapter( dvdAdapter );
        
    }
    
    private void startViewDVDActivity( long dvdId){
        
        Intent intent = new Intent(getActivity(), ViewDVDFragment.class);
        intent.putExtra( "dvdId", dvdId );
        startActivity( intent );
        
    }
}
