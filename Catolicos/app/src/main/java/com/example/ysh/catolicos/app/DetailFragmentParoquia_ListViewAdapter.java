package com.example.ysh.catolicos.app;

import android.app.ActionBar;
import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ysh.catolicos.app.data.CatolicosContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by YSH on 25/08/2015.
 */
public class DetailFragmentParoquia_ListViewAdapter extends CursorAdapter{



    /*
        This is the identifiers of diferents positions of Details items list view
    */

    private static final int VIEW_TYPE_ParoquiaAndButtons = 0;
    //private static final int VIEW_TYPE_myTodayINFO_MISSA = 1;
    private static final int VIEW_TYPE_myTodayINFO_CONF  = 1;
    private static final int VIEW_TYPE_myTodayINFO_TERC  = 2;
    private static final int VIEW_TYPE_OTHER             = 3;

    private static final int NUMBER_TYPE_OFF_DIFFERENTS_LAYOUT = 4;

    public View.OnClickListener listener;

    // Flag to determine if we want to use a separate view for "My Parish".
    private boolean mUseMyParishLayout = true;

    private static int myActualCursorPosition = 0;
    private static String myCurrentDayofWeek;
    Callback mCallback;
    ContentProvider myprovider;
    Cursor cursor_details;

    Utilities utilities = new Utilities();




    /*
        Here we connect all xml elements os each positions tipes.
    */
    public static class ViewHolderDetailView {
        /*
            About today elements of missa
        */
        public final TextView detailview_dia_semana;                //tabParish_list_item_ParishName
        public final TextView detailview_next_missa;                //tabParish_list_item_AddressParish
        public final TextView detailview_others_missa;
        public final ImageView detailview_growcalice;         //tabParish_list_item_icon_calice
        public final TextView detailview_today_activity;

        /*
            About today others elements
        */
        public final ImageView detailview_item_geral_hoje_icon;                     //tabParish_list_item_ParishName
        public final TextView detailview_item_geral_hoje_atividade;                 //tabParish_list_item_AddressParish
        public final TextView detailview_item_geral_hoje_horario;


        /*
            About General elements
        */
        public final ImageView detailview_geral_icon;                                //tabParish_list_item_ParishName
        public final TextView detailview_geral_missa;                                //tabParish_list_item_AddressParish
        public final TextView detailview_geral_confissoes;
        public final TextView detailview_geral_terco;                               //tabParish_list_item_icon_calice

        public final TextView detailview_item_geral_horamissas;
        public final TextView detailview_item_geral_horaconfissoes;
        public final TextView detailview_item_geral_horaterco;

        public final Button comochegar_button;
        public final Button info_button;


        public ViewHolderDetailView(View view)
        {

            /*
                Primeiro item da lista... com as infromacoes da missa do dia de hj em destaque
             */


            detailview_dia_semana       = (TextView) view.findViewById(R.id.detailview_dia_semana);
            detailview_next_missa       = (TextView) view.findViewById(R.id.detailview_next_missa);
            detailview_others_missa     = (TextView) view.findViewById(R.id.detailview_others_missa);
            detailview_growcalice       = (ImageView) view.findViewById(R.id.detailview_growcalice);
            detailview_today_activity   = (TextView) view.findViewById(R.id.detailview_today_activity);

            /*
                Segundo/terceiro item da lista... com as infromacoes de confiss�o/terco do dia de hj
             */
            detailview_item_geral_hoje_icon = (ImageView) view.findViewById(R.id.detailview_item_geral_hoje_icon);
            detailview_item_geral_hoje_atividade = (TextView) view.findViewById(R.id.detailview_item_geral_hoje_atividade);
            detailview_item_geral_hoje_horario = (TextView) view.findViewById(R.id.detailview_item_geral_hoje_horario);

            /*
                Quarto item da lista... com as infromacoes gerais sobre missa, confiss�es e terco
            */
            detailview_geral_icon = (ImageView) view.findViewById(R.id.detailview_geral_icon);
            detailview_geral_missa = (TextView) view.findViewById(R.id.detailview_geral_missa);
            detailview_geral_confissoes = (TextView) view.findViewById(R.id.detailview_geral_confissoes);
            detailview_geral_terco = (TextView) view.findViewById(R.id.detailview_geral_terco);

            detailview_item_geral_horamissas = (TextView) view.findViewById(R.id.detailview_item_geral_horamissas);
            detailview_item_geral_horaconfissoes = (TextView) view.findViewById(R.id.detailview_item_geral_horaconfissoes);
            detailview_item_geral_horaterco = (TextView) view.findViewById(R.id.detailview_item_geral_horaterco);

            comochegar_button = (Button) view.findViewById(R.id.como_chegar_button);
            info_button = (Button) view.findViewById(R.id.info_paroquia_button);


        }
    }

    public interface Callback{
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void showMyParishOnMap(Uri local_paroquiaURI);
        public void showMyParishInfo(String[] idParoquia);

    }


    public DetailFragmentParoquia_ListViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        myCurrentDayofWeek = new Utilities().myCurrentDayofWeek();
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int layoutId = -1;

        int viewType = getItemViewType(cursor.getPosition());


            int cursoDay = new Utilities().DayOfweek(cursor.getString(cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_DIA_SEMANA)));

            switch (viewType) {
                case VIEW_TYPE_ParoquiaAndButtons: {
                    layoutId = R.layout.list_item_detailview_paroquia;    //Item com a missa do dia em destaque
                    break;
                }
                /*

                case VIEW_TYPE_myTodayINFO_CONF: {
                    layoutId = R.layout.list_item_detail_geral_hoje;    //item com a confissao do dia em destaque;
                    break;
                }
                case VIEW_TYPE_myTodayINFO_TERC: {
                    layoutId = R.layout.list_item_detail_geral_hoje;    //item com o terco do dia em destaque;
                    break;
                }
                case VIEW_TYPE_OTHER: {
                    layoutId = R.layout.list_item_detail_other;         //item generalizado com os outro dias;
                    break;
                }
                */
                default: {
                    layoutId = R.layout.list_item_detail_geral_simples;         //item generalizado com os outro dias;
                    break;
                }
            }



        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolderDetailView viewHolder = new ViewHolderDetailView(view);
        view.setTag(viewHolder);
        return view;


    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolderDetailView viewHolder = (ViewHolderDetailView) view.getTag();


        int viewType = getItemViewType(cursor.getPosition());

        switch (viewType) {
            case VIEW_TYPE_ParoquiaAndButtons: {


                int id_AtividadePosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_ID_ATIVIDADE);
                String id_activity = cursor.getString(id_AtividadePosition);

                int diaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_DIA);
                String dia = cursor.getString(diaPosition);

                int diaSemanaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_DIA_SEMANA);
                String diaSemana = cursor.getString(diaSemanaPosition);

                int horaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_HORARIO);
                String horario = cursor.getString(horaPosition);

                int par_id = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_PAR_KEY);
                final String paroquia = cursor.getString(par_id);

                int par_address = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_ADDRESS);
                String address = cursor.getString(par_address);

                int par_city = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_CITY);
                String city = cursor.getString(par_city);

                int par_NOME = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_NOME);
                int par_REGPASTORAL = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_REGPASTORAL);
                int par_PHONE = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_PHONE);
                int par_EMAIL = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_EMAIL);
                int par_WEBPAGE = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_WEBPAGE);
                int par_ADDRESS = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_ADDRESS);
                int par_POSTALCODE = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_POSTALCODE);
                int par_CITY = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_CITY);
                int par_LATITUDE = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_LATITUDE);
                int par_LONGETUDE = cursor.getColumnIndex(CatolicosContract.ParishEntry.COLUMN_LONGETUDE);

                String arg_googlemaps =address +  ", " + city;

                //get lat long data from database **** Verificar se essa é a melhor aboradagem para permanecer.
                //todo : Maps - implementar busca dos dados de lat long no db
                //myprovider.getContext().getContentResolver();

                //float lat = (float) -23.202626;
                //float log = (float) -45.901777;
                //final String uri = "http://maps.google.com/maps?daddr=" + Float.toString(lat) + "," + Float.toString(log) + "&mode=d";

                final String uri = "http://maps.google.com/maps?daddr=" + arg_googlemaps + "&mode=d";
                final String[] par_arg = new String[10];

                par_arg[0] = cursor.getString(par_NOME);
                par_arg[1] = cursor.getString(par_REGPASTORAL);
                par_arg[2] = cursor.getString(par_PHONE);
                par_arg[3] = cursor.getString(par_EMAIL);
                par_arg[4] = cursor.getString(par_WEBPAGE);
                par_arg[5] = cursor.getString(par_ADDRESS);
                par_arg[6] = cursor.getString(par_POSTALCODE);
                par_arg[7] = cursor.getString(par_CITY);
                par_arg[8] = cursor.getString(par_LATITUDE);
                par_arg[9] = cursor.getString(par_LONGETUDE);

                viewHolder.detailview_dia_semana.setText(diaSemana);

                /*
                    Organizando as informações para serem mostradas.
                */
                String hour_list = format_first(horario);

                //String[] myHours = get_mainhour(hour_list);

                viewHolder.detailview_next_missa.setText(hour_list);
               //viewHolder.detailview_others_missa.setText(horario);
                viewHolder.detailview_today_activity.setText(id_activity);

                viewHolder.comochegar_button.setOnClickListener(this.listener);
                viewHolder.info_button.setOnClickListener(this.listener);

                viewHolder.comochegar_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Log.v("Adapt", "CLIQUE1");
                        /*
                            Montando listview
                        */
                        Uri gmmIntentUri = Uri.parse(uri);
                        try {
                            mCallback = (Callback) context;
                        } catch (ClassCastException e) {
                            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
                        }
                        ((Callback) context).showMyParishOnMap(gmmIntentUri);

                    }
                });

                viewHolder.info_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        try {
                            mCallback = (Callback) context;
                        } catch (ClassCastException e) {
                            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
                        }
                        ((Callback) context).showMyParishInfo(par_arg);

                    }
                });

                //viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;

            }

            /*
            case VIEW_TYPE_myTodayINFO_CONF: {
                //viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }

            case VIEW_TYPE_myTodayINFO_TERC: {
                //viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }


            case VIEW_TYPE_OTHER: {
                //viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }

            */

            default:{

                int id_AtividadePosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_ID_ATIVIDADE);
                String id_activity = cursor.getString(id_AtividadePosition);

                int diaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_DIA);
                String dia = cursor.getString(diaPosition);

                int diaSemanaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_DIA_SEMANA);
                String diaSemana = cursor.getString(diaSemanaPosition);

                int horaPosition = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_HORARIO);
                String horario = cursor.getString(horaPosition);
                String hour_list = format_first(horario);

                int par_id = cursor.getColumnIndex(CatolicosContract.ActivityEntry.COLUMN_PAR_KEY);
                String paroquia = cursor.getString(par_id);

                viewHolder.detailview_geral_missa.setText(diaSemana);
                viewHolder.detailview_item_geral_horamissas.setText(hour_list);

                break;
            }



        }

        /*
        String mparish_name = cursor.getString(tab_paroquias.COL_PARISH_NOME);
        viewHolder.parish_name.setText(mparish_name);
        String mparish_address = cursor.getString(tab_paroquias.COL_PARISH_ADDRESS);
        viewHolder.parish_address.setText(mparish_address);
        viewHolder.calice_iconView.setImageResource(R.drawable.ic_calice);
        viewHolder.calice_iconView.setImageAlpha(255);

        viewHolder.hands_iconView.setImageResource(R.drawable.ic_hands);
        viewHolder.hands_iconView.setImageAlpha(255);

        viewHolder.rosario_iconView.setImageResource(R.drawable.ic_terco);
        viewHolder.rosario_iconView.setImageAlpha(255);
        */



    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_TYPE_ParoquiaAndButtons;
        }else if(position == 1){
            return VIEW_TYPE_myTodayINFO_CONF;
        }else if(position == 2){
            return VIEW_TYPE_myTodayINFO_TERC;
        }
        else
            return VIEW_TYPE_OTHER;
    }


    /*
        Aqui retornamos o numero de diferentes tipos de layouts que teremos.
     */
    @Override
    public int getViewTypeCount() {
        return NUMBER_TYPE_OFF_DIFFERENTS_LAYOUT;
    }

    public String[] get_mainhour(String[] array)  {

        String[] Ordered_hour = new String[array.length];

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        /*
            Registra a hora corrente para fazer comparação com o vetor
         */
        int CurrentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String time2 = "22:22";

        String m = String.format(time2,"HH:mm");

        int size_off_array = array.length;

        Date vHourMissa = new Date();

        for(int i = 0; i < size_off_array ; i++) {

            try{
                vHourMissa = format.parse(String.valueOf(array[i]));
            } catch (final ParseException e) {
                e.printStackTrace();
            }

            vHourMissa.getHours();

            if(vHourMissa.getHours() >= CurrentHour){
                Ordered_hour[i] = array[i];
            }else{

            }

        }

        return Ordered_hour;
    }


    public String format_first(String horario) {
        horario = horario.replace("[", "");
        horario = horario.replace("]", "");

        //List<String> hourList = Arrays.asList(horario.split(","));
        //String[] array = hourList.toArray(new String[hourList.size()]);
        return horario;
    }



}