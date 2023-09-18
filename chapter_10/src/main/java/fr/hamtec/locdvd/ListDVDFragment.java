package fr.hamtec.locdvd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ListDVDFragment extends Fragment {
    private ListView list;
    private OnDVDSelectedListener onDVDSelectedListener;
    public interface OnDVDSelectedListener{
        public void onDVDSelected(long dvdId);
    }
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_listdvd, container, false );
        list = view.findViewById( R.id.main_List );
        list.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView < ? > parent, View view, int position, long id ) {
                if ( onDVDSelectedListener !=null ){
                    DVD selectedDvd = (DVD ) view.getTag();
                    onDVDSelectedListener.onDVDSelected( selectedDvd.getId() );
                }
            }
        } );
        return view;
    }
    @Override
    public void onAttach(  Context activity ) {
        super.onAttach( activity );
        onDVDSelectedListener = ( OnDVDSelectedListener ) activity;
    }
    private void startViewDVDActivity( long dvdId ) {
        Intent intent = new Intent( getActivity(), ViewDVDFragment.class );
        intent.putExtra( "dvdId", dvdId );
        startActivity( intent );
    }
    @Override
    public void onResume( ) {
        super.onResume( );
        updateDVDList();
    }
    public void updateDVDList(){
        ArrayList<DVD> dvdList = DVD.getDVDList( getActivity() );
        DVDAdapter dvdAdapter = new DVDAdapter( getActivity(), dvdList );
        list.setAdapter( dvdAdapter );
    }
}
