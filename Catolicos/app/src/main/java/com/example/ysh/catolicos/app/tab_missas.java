package com.example.ysh.catolicos.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ysh.catolicos.app.sync.CatolicosSyncAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
  Created by YSH on 25/05/2015.
 */
public class tab_missas extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final String LOG_TAG = tab_missas.class.getSimpleName();
    private tab_missas_Adapter mForecastAdapter;


    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

                //Criando um adapter
                ArrayAdapter<String> mAdapter;

    private tab_missas_Adapter mTabMissasAdapter;


    private ListView mListView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            mTabMissasAdapter = new tab_missas_Adapter(getActivity(), null, 0);
            View rootView = inflater.inflate(R.layout.missas, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listview_missas);
            mListView.setAdapter(mTabMissasAdapter);

            return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}