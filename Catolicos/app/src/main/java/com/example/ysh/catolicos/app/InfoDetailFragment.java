package com.example.ysh.catolicos.app;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ysh.catolicos.app.data.CatolicosContract;

/**
 * Created by YSH on 22/08/2015.
 */

public class InfoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public final String LOG_TAG = DetailActivityParoquia.class.getSimpleName();

    String ParoquiaDetail;
    private static final int DETAIL_LOADER = 0;

    public InfoDetailFragment() {
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
            //ParoquiaDetail = arguments.getString(String.valueOf(R.string.Intent_detailview_paroquia));
        }

        if (savedInstanceState != null) {
            //mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        View rootView = inflater.inflate(R.layout.infodetail_paroquia, container, false);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey(String.valueOf(R.string.Intent_detailview_paroquia))){
            //    getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
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
            //    getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
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
