package com.example.ysh.catolicos.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YSH on 16/06/2015.
 */
public class tab_missas_Adapter extends CursorAdapter {


    private static final int VIEW_TYPE_MYPARISH = 0;
    private static final int VIEW_TYPE_OTHERPARISH = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    // Flag to determine if we want to use a separate view for "My Parish".
    private boolean mUseMyParishLayout = true;

    public tab_missas_Adapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        Impementa todas as widgets presentes dentro de uma listview
     */
    public static class ViewHolder {
        /*
            Conecta todos os widgets criadas nessa Classe e suas referencias em /Res [resources]
        */
        public ViewHolder(View view) {

        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_MYPARISH: {
                layoutId = R.layout.list_item_detail_geral_simples; //list_item_missas_MyParish;
                break;
            }
            case VIEW_TYPE_OTHERPARISH: {
                layoutId = R.layout.list_item_detail_geral_simples;
                break;
            }
            default: {
                layoutId = R.layout.list_item_detail_geral_simples;         //item generalizado com os outro dias;
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
            case VIEW_TYPE_MYPARISH: {
                // Get weather icon
                //viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
            case VIEW_TYPE_OTHERPARISH: {
                // Get weather icon
                //viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }


    }
}
