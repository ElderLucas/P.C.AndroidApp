package com.example.ysh.catolicos.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    //Criando um adapter
    ArrayAdapter<String> mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] data = {
                "Catedral - 19h00 - Sexta Feira",
                "NS Aparecida - 19h00 - Sexta Feira",
                "Catedral - 19h00 - Sexta Feira",
                "Sagrada - 19h00 - Sexta Feira",
                "Santana - 19h00 - Sexta Feira",
                "Jaguary - 19h00 - Sexta Feira",
                "Jacarei - 19h00 - Sexta Feira",
                "S j D Bosco - 19h00 - Sexta Feira",
                "NS Fatima - 19h00 - Sexta Feira",
                "Missa - 19h00 - Sexta Feira",
                "Missa - 19h00 - Sexta Feira"

        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        mAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_missas, // The name of the layout ID.
                R.id.list_item_textview_missa, // The ID of the textview to populate.
                weekForecast);

        View v =inflater.inflate(R.layout.missas,container,false);

        ListView listView = (ListView) v.findViewById(R.id.listview_missas);
        listView.setAdapter(mAdapter);

        return v;
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