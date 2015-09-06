package com.example.ysh.catolicos.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/*
  Created by YSH on 27/05/2015.
 */
public class CatolicosContract {

    public static final String segunda = "Segunda-feira";
    public static final String terca   = "Terça-feira";
    public static final String quarta  = "Quarta-feira";
    public static final String quinta  = "Quinta-feira";
    public static final String sexta   = "Sexta-feira";
    public static final String sabado  = "Sábado";
    public static final String domingo = "Domingo";
     /*
        Query to access my Google Cloud SQL database
        http://104.154.84.179/atividades.php?paroquia=ParishName&dia=WeekDay
        for example  ->  http://104.154.84.179/atividades.php?paroquia=CatedralSaoDimas&dia=quarta
     */

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.ysh.catolicos.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final Uri mBASE_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    // Possible paths (appended to base content URI for possible URI's)

    /*
        content://com.example.ysh.catolicos.app/paroquia/ is a valid path for access Parish Data
    */
    public static final String PATH_PARISH  = "paroquia";

    /*
        content://com.example.ysh.catolicos.app/atividade/ is a valid path for access Activity Data
    */
    public static final String PATH_ACTIVITY = "atividade";


    private static final int PARISHTABLE_LOADER = 0;

    /*
        As colunas do Parish e Activity Tables
    */
    public static final String[] PARISH_COLUMNS = {
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

    public static final String[] ACTIVITY_COLUMNS = {
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

    public static final String[] ACTIVITY_COLUMNS_detailView = {
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
            ActivityEntry.COLUMN_HORARIO,

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

    /*
        Inner class that defines the table contents of the Paroquia table
    */
    public static final class ParishEntry implements BaseColumns {

        public static final Uri    CONTENT_URI         = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARISH).build();

        /*
        URI containing a Cursor of zero or more items.
         */
        public static final String CONTENT_TYPE        = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARISH;

        /*
        URI containing a Cursor of a single item
         */
        public static final String CONTENT_ITEM_TYPE   = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARISH;


        public static final String TABLE_NAME          = "Parish";
        public static final String COLUMN_ID_PAROQUIA  = "ID_Paroquia";
        public static final String COLUMN_NOME         = "Nome";
        public static final String COLUMN_REGPASTORAL  = "RegPastoral";
        public static final String COLUMN_PHONE        = "Phone";
        public static final String COLUMN_EMAIL        = "email";
        public static final String COLUMN_WEBPAGE      = "webpage";
        public static final String COLUMN_ADDRESS      = "address";
        public static final String COLUMN_POSTALCODE   = "postal_code";
        public static final String COLUMN_CITY         = "city";
        public static final String COLUMN_LATITUDE     = "latitude";
        public static final String COLUMN_LONGETUDE    = "longitude";

        public static Uri buildParishUri(long arg1) {

            String _id = String.valueOf(arg1);

            return CONTENT_URI
                    .buildUpon()
                    .appendPath(_id)
                    .build();
        }

        public static Uri build_ParishWithNameURi(String arg1){
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(arg1)
                    .build();
        }


        public static Uri build_ParishWithLocationURi(String arg1, String arg2){
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(arg1)
                    .appendPath(arg2)
                    .build();
        }

        public static Uri buildUri() {
            return CONTENT_URI
                    .buildUpon()
                    .build();
        }
    }

    /*
        Inner class that defines the table contents of the ParishActivity table
    */
    public static final class ActivityEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTIVITY).build();
        /*
        URI containing a Cursor of zero or more items.
        */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIVITY;

        /*
        URI containing a Cursor of a single item
        */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIVITY;

        public static final String TABLE_NAME           = "Activity";
        public static final String COLUMN_PAR_KEY       = "id_Par";
        public static final String COLUMN_ID_ATIVIDADE  = "atividade";
        public static final String COLUMN_DIA           = "dia";
        public static final String COLUMN_DIA_SEMANA    = "dia_semana";
        public static final String COLUMN_HORARIO       = "horario";

        public static Uri buildActivityUri(long arg1) {

            String _id = String.valueOf(arg1);

            return CONTENT_URI
                    .buildUpon()
                    .appendPath(_id)
                    .build();
        }

        /*
           resulte is all activities of parish
        */
        public static Uri build_ActivityParish(String arg1) {
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(arg1)
                    .build();
        }

        /*
            resulte is all activities of parish in a weeak day
        */
        public static Uri build_ActivityParishWithWeekDay(String arg1, String arg2) {
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(arg1)
                    .appendPath(arg2)
                    .build();
        }


        public static Uri buildUri() {
            return CONTENT_URI
                    .buildUpon()
                    .build();
        }

        /*
            Return a parish name of my URi
        */
        public static String getParishFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        /*
            Return a Day of my URi
        */
        public static String getDayFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }


}
