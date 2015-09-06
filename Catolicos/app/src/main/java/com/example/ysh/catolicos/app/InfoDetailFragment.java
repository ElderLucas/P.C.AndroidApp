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
import android.widget.TextView;

import com.example.ysh.catolicos.app.data.CatolicosContract;

/**
 * Created by YSH on 22/08/2015.
 */

public class InfoDetailFragment extends Fragment{


    public final String LOG_TAG = DetailActivityParoquia.class.getSimpleName();

    String ParoquiaDetail;
    String[] ParoquiaDetailArray;

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
            String[] details = arguments.getStringArray(String.valueOf(R.string.info_detail_paroquia));
            ParoquiaDetailArray = details;
        }

        if (savedInstanceState != null) {
            //mLocation = savedInstanceState.getString(LOCATION_KEY);
        }

        View rootView = inflater.inflate(R.layout.infodetail_paroquia, container, false);

        TextView nome_textview = (TextView) rootView.findViewById(R.id.id_textview_Nome);
        TextView regpastoral_textview = (TextView) rootView.findViewById(R.id.id_textview_REGPASTORAL);

        TextView phone_textview = (TextView) rootView.findViewById(R.id.id_textview_PHONE);
        TextView email_textview = (TextView) rootView.findViewById(R.id.id_textview_EMAIL);
        TextView webpage_textview = (TextView) rootView.findViewById(R.id.id_textview_WEBPAGE);
        TextView address_textview = (TextView) rootView.findViewById(R.id.id_textview_ADDRESS);
        TextView postalcode_textview = (TextView) rootView.findViewById(R.id.id_textview_POSTALCODE);
        TextView city_textview = (TextView) rootView.findViewById(R.id.id_textview_CITY);

        nome_textview.setText(ParoquiaDetailArray[0]);
        regpastoral_textview.setText(ParoquiaDetailArray[1]);
        phone_textview.setText(ParoquiaDetailArray[2]);
        email_textview.setText(ParoquiaDetailArray[3]);
        webpage_textview.setText(ParoquiaDetailArray[4]);
        address_textview.setText(ParoquiaDetailArray[5]);
        postalcode_textview.setText(ParoquiaDetailArray[6]);
        city_textview.setText(ParoquiaDetailArray[7]);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();

        /*
        if (arguments != null && arguments.containsKey(String.valueOf(R.string.Intent_detailview_paroquia))){
                getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
        */




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



}
