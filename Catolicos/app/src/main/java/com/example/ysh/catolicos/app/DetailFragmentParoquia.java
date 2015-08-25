package com.example.ysh.catolicos.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        View rootView = inflater.inflate(R.layout.fragment_detail_paroquia, container, false);


        /*
            Tratando o Botão Como Chegar
         */
        Button comochegar_button = (Button) rootView.findViewById(R.id.como_chegar_button);
        Button info_button = (Button) rootView.findViewById(R.id.info_paroquia_button);

        comochegar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "Botao Como chegar");

               // Uri gmmIntentUri = Uri.parse("google.maps:q=-23.202626,-45.901777&mode=off");  //-23.202471, -45.901424

                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=-23.202626,-45.901777&mode=d");


                try {
                    mCallback = (Callback) getActivity();
                } catch (ClassCastException e) {
                    Log.e(LOG_TAG, "Call Back Detail error");
                    throw new ClassCastException(getActivity().toString() + " must implement OnHeadlineSelectedListener");

                }

                ((Callback) getActivity()).showMyParishOnMap(gmmIntentUri);
            }
        });


        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "Botao Info");

                try {
                    mCallback = (Callback) getActivity();
                } catch (ClassCastException e) {
                    Log.e(LOG_TAG, "Call Back Detail error");
                    throw new ClassCastException(getActivity().toString() + " must implement OnHeadlineSelectedListener");
                }
                ((Callback) getActivity()).showMyParishInfo("MyParish");

            }
        });

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
                CatolicosContract.ACTIVITY_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v("DetailParoquia", "LoadFinished ##########");

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
