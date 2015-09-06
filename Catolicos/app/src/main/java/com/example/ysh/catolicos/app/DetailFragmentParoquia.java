package com.example.ysh.catolicos.app;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ysh.catolicos.app.data.CatolicosContract;

/**
 * Created by YSH on 10/08/2015.
 */
public class DetailFragmentParoquia extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public final String LOG_TAG = DetailActivityParoquia.class.getSimpleName();

    String mParoquiaStr;
    String ParoquiaDetail;
    private static final int DETAIL_LOADER = 0;
    Callback mCallback;

    Uri ParishUri = CatolicosContract.ParishEntry.CONTENT_URI;
    Uri ActivityUri = CatolicosContract.ActivityEntry.CONTENT_URI;

    /*
        Adapatador para a list view da details parish list view
    */
    private DetailFragmentParoquia_ListViewAdapter mAdapter;
    private ListView mListView;

    /*
        Interface com activity
    */
    public interface Callback{
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void showMyParishOnMap(Uri local_paroquiaURI);

        public void showMyParishInfo(String idParoquia);
    }


    public DetailFragmentParoquia() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            ParoquiaDetail = arguments.getString(String.valueOf(R.string.Intent_detailview_paroquia));
        }

        if (savedInstanceState != null) {
            //mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        /*
            Aqui eu registro o ContentObserver para "escutar" mudanças no content provider
        */
        MyContentObserver observer = new MyContentObserver(null);

       /*
           Registra o Content observer refernrete ao notifyChange(uri... que é "setado" dentro do contente Observer
       */
       this.getActivity().getContentResolver().registerContentObserver(ActivityUri, true, observer);

       /*
           Inicializa o adapter
       */
       mAdapter = new DetailFragmentParoquia_ListViewAdapter(getActivity(), null, 0);
       View rootView = inflater.inflate(R.layout.fragment_detail_paroquia, container, false);
       mListView = (ListView) rootView.findViewById(R.id.listview_detail_paroquia);
       mListView.setAdapter(mAdapter);

       /*
       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long viewId = view.getId();

                    if (viewId == R.id.como_chegar_button) {
                        Toast.makeText(getActivity(),"Comochegar", Toast.LENGTH_SHORT );
                    } else if (viewId == R.id.info_paroquia_button) {
                        Toast.makeText(getActivity(), "Info", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(getActivity(), "Outro", Toast.LENGTH_SHORT);
                    }
                }
            });
        */
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey(String.valueOf(R.string.Intent_detailview_paroquia))){
            //getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(String.valueOf(R.string.Intent_detailview_paroquia))) {
            init_loader();
            //getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }




    private void init_loader() {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    private void restart_loader() {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri ActivityParoquiaURi = CatolicosContract.ActivityEntry.build_ActivityParish(ParoquiaDetail);

        // Now create and return a CursorLoader that will take care of creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                ActivityParoquiaURi,
                CatolicosContract.ACTIVITY_COLUMNS_detailView,//CatolicosContract.ACTIVITY_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }


    /*
    Content Observer que recebe notificacoes de alteracoes na URI que foi registrada para observacao.
    O Registro da notificacao da URI é feita no content provider.
*/
    class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler h) {
            super(h);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        /*
            Estou notificando as uri de paroquia e atividade. Por implementacao, estou apenas as notificando a tab atividaed prq na pratica incluo paroquias e depois os horarios.
         */
        @Override
        public void onChange(boolean selfChange) {

            Log.v(LOG_TAG, " ########## onChange(" + selfChange + ")");
            super.onChange(selfChange);
            // todo: here you call the method to fill the list
            // todo : Sinalizar para o loader cursor que ele deve performar uma query

            //updateParish();

        }
    }




}
