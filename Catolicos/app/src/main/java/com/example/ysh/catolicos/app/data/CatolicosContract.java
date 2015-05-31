package com.example.ysh.catolicos.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by YSH on 27/05/2015.
 */
public class CatolicosContract {

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

        public static Uri buildParishUri() {
            Uri myURIParish = CONTENT_URI;
            return myURIParish;
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

        public static final String TABLE_NAME           = "ParishActivity";
        public static final String COLUMN_PAR_KEY       = "id_Par";
        public static final String COLUMN_ID_ATIVIDADE  = "atividade";
        public static final String COLUMN_DIA           = "dia";
        public static final String COLUMN_DIA_SEMANA    = "dia_semana";
        public static final String COLUMN_HORARIO       = "horario";

        public static Uri buildActivityUri() {
            Uri myURIActivit = CONTENT_URI;
            return myURIActivit;
        }

        /*
           resulte is all activities of parish
        */
        public static Uri build_ActivityParish(String Parish) {
            Uri myURIActivity = CONTENT_URI.buildUpon().appendPath(Parish).build();
            return myURIActivity;
        }

        /*
            resulte is all activities of parish in a weeak day
        */
        public static Uri build_ActivityParishWithWeekDay(String Parish, String day) {
            Uri myURIActivitParishWithWeekDay = CONTENT_URI.buildUpon()
                    .appendPath(Parish)
                    .appendQueryParameter(COLUMN_PAR_KEY, Parish)
                    .appendQueryParameter(COLUMN_DIA, day).build();
            return myURIActivitParishWithWeekDay;
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

        public static String getParishNameFromURi(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static String getDayWeedFromURi(Uri uri){
            return uri.getPathSegments().get(2);
        }

    }

}
