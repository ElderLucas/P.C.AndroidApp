package com.example.ysh.catolicos.app;

import android.app.FragmentManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.ysh.catolicos.app.data.CatolicosContract;
import com.example.ysh.catolicos.app.data.CatolicosContract.ParishEntry;
import com.example.ysh.catolicos.app.data.CatolicosContract.ActivityEntry;
import com.example.ysh.catolicos.app.sync.CatolicosSyncAdapter;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;

import java.io.UTFDataFormatException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by YSH on 25/05/2015.
 */
public class tab_paroquias extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
        Implementando o callback
    */
    Callback mCallback;



    public final String LOG_TAG = tab_paroquias.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    //Criando um adapter
    ArrayAdapter<String> mAdapter;

    /*
        Adapatarod para a list view da parish view
     */
    private tab_paroquias_Adapter mTabParishAdapter;


    private ListView mListView;
    /*
        Essa variavel é usada para indicar qual foi a linha selecionada no toque da tela....
     */
    private int mPosition = ListView.INVALID_POSITION;

    /*
        Paroqui de origem ou a paróqui preferida pelo usuário
        todo: Implementar futuramente a verificacao GPS do local do usuario e indicar a paroqui mais proxima
    */
    private String mParish = "Matriz";

    static final String TEST_ACTIVITY_PARISH = "SaoDimas";

    //Uri ActivityUri = CatolicosContract.ActivityEntry.buildUri();

    //Uri ParishUri = CatolicosContract.ParishEntry.buildUri();

    Uri ParishUri = CatolicosContract.ParishEntry.CONTENT_URI;
    Uri ActivityUri = CatolicosContract.ActivityEntry.CONTENT_URI;

    private static final int PARISHTABLE_LOADER = 0;

    /*
        As colunas do Parish e Activity Tables
    */
    private static final String[] PARISH_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            ParishEntry.TABLE_NAME + "." + ParishEntry._ID,
            ParishEntry.COLUMN_ID_PAROQUIA,
            ParishEntry.COLUMN_NOME,
            ParishEntry.COLUMN_REGPASTORAL,
            ParishEntry.COLUMN_PHONE,
            ParishEntry.COLUMN_EMAIL,
            ParishEntry.COLUMN_WEBPAGE,
            ParishEntry.COLUMN_ADDRESS,
            ParishEntry.COLUMN_POSTALCODE,
            ParishEntry.COLUMN_CITY,
            ParishEntry.COLUMN_LATITUDE,
            ParishEntry.COLUMN_LONGETUDE
    };

    private static final String[] ACTIVITY_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            ActivityEntry.TABLE_NAME + "." + ActivityEntry._ID,
            ActivityEntry.COLUMN_PAR_KEY,
            ActivityEntry.COLUMN_ID_ATIVIDADE,
            ActivityEntry.COLUMN_DIA,
            ActivityEntry.COLUMN_DIA_SEMANA,
            ActivityEntry.COLUMN_HORARIO
    };

    //These indices are tied to PARISH_COLUMNS.  If PARISH_COLUMNS changes, these must change.
    public static final int COL_PARISH_ID_PAROQUIA = 1;
    public static final int COL_PARISH_NOME = 2;
    public static final int COL_PARISH_REGPASTORAL  = 3;
    public static final int COL_PARISH_PHONE = 4;
    public static final int COL_PARISH_EMAIL = 5;
    public static final int COL_PARISH_WEBPAGE = 6;
    public static final int COL_PARISH_ADDRESS = 7;
    public static final int COL_PARISH_POSTALCODE = 8;
    public static final int COL_PARISH_CITY = 9;
    public static final int COL_PARISH_LATITUDE = 10;
    public static final int COL_PARISH_LONGETUDE = 11;

    //These indices are tied to ACTIVITY_COLUMNS.  If ACTIVITY_COLUMNS changes, these must change.
    public static final int COL_ACTIVITY_PAR_KEY = 1;
    public static final int COL_ACTIVITY_ID_ATIVIDADE = 2;
    public static final int COL_ACTIVITY_DIA = 3;
    public static final int COL_ACTIVITY_DIA_SEMANA = 4;
    public static final int COL_ACTIVITY_HORARIO = 5;



    //********************************************************************************************//
    //                               Implementacoes dos Metodos                                   //
    //********************************************************************************************//
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String date);
    }


    /*
        Para criar uma Circular View, usar o post https://github.com/lopspower/CircularImageView
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
            Aqui eu registro o ContentObserver para "escutar" mudanças no content provider
        */
        MyContentObserver observer = new MyContentObserver(null);

        /*
            Registra o Content observer refernrete ao notifyChange(uri... que é "setado" dentro do contente Observer
        */
        this.getActivity().getContentResolver().registerContentObserver(ParishUri, true, observer);

        /*
            Inicializa o adapter
        */
        mTabParishAdapter = new tab_paroquias_Adapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.paroquias, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_paroquias);
        mListView.setAdapter(mTabParishAdapter);

        /*
            Set Listener
        */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                Cursor cursor = mTabParishAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {

                    Log.v(LOG_TAG, "CLIQUE OK --- Cursor sem problema - onItemCliqued");
                    // This makes sure that the container activity has implemented
                    // the callback interface. If not, it throws an exception
                    try {
                        mCallback = (Callback) getActivity();
                    } catch (ClassCastException e) {
                        Log.e(LOG_TAG, "CAll Back Mai Erro..... **************############");
                        throw new ClassCastException(getActivity().toString() + " must implement OnHeadlineSelectedListener");

                    }

                   ((Callback) getActivity()).onItemSelected(cursor.getString(COL_PARISH_ID_PAROQUIA));
                   // getFragmentManager().beginTransaction().add(my,"my").addToBackStack().commit()
                }
                else{
                    Log.e(LOG_TAG, "CLIQUE ERRADO Curso problema- onItemCliqued");
                }

                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        /*
            Registra e inicializa o Loader
        */
        init_loader();
        super.onActivityCreated(savedInstanceState);
    }

    private void init_loader() {
        getLoaderManager().initLoader(PARISHTABLE_LOADER, null, this);
    }

    private void restart_loader() {
        getLoaderManager().restartLoader(PARISHTABLE_LOADER, null, this);
    }

    private void updateParish() {
        //todo: Implementar uma chamada para realizar uma sicronia ou "download" imediato das informacoes do servidor que preencheram nossa listview
        restart_loader();
    }

    @Override
    public void onResume() {
        super.onResume();

        //if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
        //todo : Aqui eu devo verificar se minha paroquia de preferencia foi alterada..... A verificacao devera ser feita no parametro do preferences file
        restart_loader();
        //}
    }

    /*
        Metodo para guardar a posicao que foi deixada a list view.
        Caso se saia da tela e volte, nao sera preciso buscar a posicao em que foi deixado na current tela
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    /*
        Content Observer que recebe notificacoes de alteracoes na URI que foi registrada para observacao.
        O Registro da notificacao da URI é feita no content provider.
    */
    class MyContentObserver extends ContentObserver{
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


    /*
        O primeiro metodo Callback que sera chamado nessa classe de loader cursor sera o metodo de createLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, get the String representation for today,
        // and filter the query to return weather only for dates after or including today.
        // Only return data after today.

        //todo - no nosso caso, nos mostraremos todas as paroquias e suas informacoes....
        //porem, sera necessario mostrar algumas informacoes que necessitam dos horarios das missas

        //String startDate = WeatherContract.getDbDateString(new Date());
        //String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";
        //mLocation = Utility.getPreferredLocation(getActivity());
        //Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
        //        mLocation, startDate);

        // Create and return a basic URI of Parish table
        Uri AllParishUri = ParishEntry.buildUri();

        // Now create and return a CursorLoader that will take care of creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                AllParishUri,
                PARISH_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "LoadFinished ##########");

        mTabParishAdapter.swapCursor(data);
        //todo - implementar o smooth control para a posicao que nos interresa

        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            //mListView.smoothScrollToPosition(mPosition);
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTabParishAdapter.swapCursor(null);
    }


}