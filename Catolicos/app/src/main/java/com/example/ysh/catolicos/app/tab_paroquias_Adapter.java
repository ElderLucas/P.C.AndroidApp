package com.example.ysh.catolicos.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Created by YSH on 18/07/2015.
 */
public class tab_paroquias_Adapter extends CursorAdapter {

    private static final int VIEW_TYPE_MYPARIH = 0;
    private static final int VIEW_TYPE_OTHER = 1;


    // Flag to determine if we want to use a separate view for "My Parish".
    private boolean mUseMyParishLayout = true;

    /*
        Impementa todas as widgets presentes dentro de uma listview
     */
    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {

        public final TextView parish_name;              //tabParish_list_item_ParishName
        public final TextView parish_address;           //tabParish_list_item_AddressParish

        public final ImageView calice_iconView;         //tabParish_list_item_icon_calice
        public final ImageView hands_iconView;          //tabParish_list_item_icon_hands
        public final ImageView rosario_iconView;        //tabParish_list_item_rosario

        public ViewHolder(View view) {
            parish_name      = (TextView) view.findViewById(R.id.tabParish_list_item_ParishName);
            parish_address   = (TextView) view.findViewById(R.id.tabParish_list_item_AddressParish);
            calice_iconView  = (ImageView) view.findViewById(R.id.tabParish_list_item_icon_calice);
            hands_iconView   = (ImageView) view.findViewById(R.id.tabParish_list_item_icon_hands);
            rosario_iconView = (ImageView) view.findViewById(R.id.tabParish_list_item_rosario);
        }
    }


    public tab_paroquias_Adapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_MYPARIH: {
                layoutId = R.layout.list_item_paroquias; //list_item_missas_MyParish;
                break;
            }
            case VIEW_TYPE_OTHER: {
                layoutId = R.layout.list_item_paroquias;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_MYPARIH: {
                // Get weather icon
                //viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
            case VIEW_TYPE_OTHER: {
                // Get weather icon
                //viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }

        /*
            Seta o nome da paroqui no tinem da list view
        */
        String mparish_name = cursor.getString(tab_paroquias.COL_PARISH_NOME);
        viewHolder.parish_name.setText(mparish_name);


        /*
            Seta o Endereco da paroquia no tinem da list view
        */
        String mparish_address = cursor.getString(tab_paroquias.COL_PARISH_ADDRESS);
        viewHolder.parish_address.setText(mparish_address);


        /*
            Seta os tres icones do item na list view
            *Image Alpha aceita de 0 ... 255
        */
        viewHolder.calice_iconView.setImageResource(R.drawable.ic_calice);
        viewHolder.calice_iconView.setImageAlpha(255);

        viewHolder.hands_iconView.setImageResource(R.drawable.ic_hands);
        viewHolder.hands_iconView.setImageAlpha(255);

        viewHolder.rosario_iconView.setImageResource(R.drawable.ic_terco);
        viewHolder.rosario_iconView.setImageAlpha(255);

    }
}