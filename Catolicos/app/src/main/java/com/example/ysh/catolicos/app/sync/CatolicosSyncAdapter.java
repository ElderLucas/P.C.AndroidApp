package com.example.ysh.catolicos.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;

import com.example.ysh.catolicos.app.MainActivity;
import com.example.ysh.catolicos.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;

/*
    Created by YSH on 13/06/2015.


    Downloading and uploading data
    A sync adapter doesn't automate any data transfer tasks.
    If you want to download data from a server and store it in a content provider,
    you have to provide the code that requests the data, downloads it,
    and inserts it in the provider. Similarly, if you want to send data to a server,
    you have to read it from a file, database, or provider, and send the necessary upload request.
    You also have to handle network errors that occur while your data transfer is running.

    Handling data conflicts or determining how current the data is
    A sync adapter doesn't automatically handle conflicts between data on the server and data on the device.
    Also, it doesn't automatically detect if the data on the server is newer than the data on the device,
    or vice versa. Instead, you have to provide your own algorithms for handling this situation.
 */
public class CatolicosSyncAdapter extends AbstractThreadedSyncAdapter {


    /*
    Read Information here: https://developer.android.com/training/sync-adapters/creating-sync-adapter.html#CreateSyncAdapter
     */
    public final String LOG_TAG = CatolicosSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_INTERVAL_debug = 3;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public static final int SYNC_FLEXTIME_debug = SYNC_INTERVAL_debug/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int PARISH_NOTIFICATION_ID = 3004;

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /*
    Creating a constructor
     Used to run setup task each time your
     */
    public CatolicosSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public CatolicosSyncAdapter(Context context,boolean autoInitialize,boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
        Data transfer code:
        When the framework is ready to sync the application's data, it invokes the implementation of the method onPerformSync().
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.d(LOG_TAG, "Starting sync");

        /*
            Get Preference My Parish
        */
        String ParishQuery = "CatedralSaoDimas"; //Utility.getPreferredLocation(getContext());

        /*
            Will contain the raw JSON response as a string.
        */
        String parishJsonStr = null;
        /*
            These two need to be declared outside the try/catch so that they can be closed in the finally block.
        */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        /*
            Will contain the raw JSON response as a string.
        */
        String ActivityParishJsonStr = null;

        String Parish = "json";
        String units = "metric";
        int numDays = 14;

        try {

            /*
                URL Base
             */
            Log.d(LOG_TAG, "Starting sync");
            final String FORECAST_BASE_URL = "http://104.154.84.179/atividades.php?";

            final String PARISH_QUERY_PARAM = "paroquia";
            final String DAY_PARAM = "dia";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    //.appendQueryParameter(PARISH_QUERY_PARAM, ParishQuery)
                    .build();

            URL url = new URL(builtUri.toString());

            /*
                Connecting to a server
             */
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            parishJsonStr = buffer.toString();

            String mytext = stripHtml(parishJsonStr); //http://stackoverflow.com/questions/6502759/how-to-strip-or-escape-html-tags-in-android

            getWeatheDataFromJson(mytext, ParishQuery);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            e.printStackTrace();

            /*
            Clean up: Always close connections to a server and clean up temp files and caches at the end of your data transfer.
            */
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getWeatheDataFromJson(String parishJsonStr, String locationSetting) throws JSONException {

        try {

            JSONArray jarray = new JSONArray(parishJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(jarray.length());

            for(int i = 0; i < jarray.length(); i++) {
                JSONObject TodasParoquiasJSONOBJ = jarray.toJSONObject(jarray);
                Log.d(LOG_TAG,"c");
             }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }


    }


    /*
        Notificacao das informacoes do Catolicos
     */

    /*
    private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {


                // Last sync was more than 1 day ago, let's send a notification with the weather.
                String locationQuery = Utility.getPreferredLocation(context);

                Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

                // We'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {

                    int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                    double high = cursor.getDouble(INDEX_MAX_TEMP);
                    double low = cursor.getDouble(INDEX_MIN_TEMP);
                    String desc = cursor.getString(INDEX_SHORT_DESC);

                    int iconId = Utility.getIconResourceForWeatherCondition(weatherId);

                    Resources resources = context.getResources();
                    Bitmap largeIcon = BitmapFactory.decodeResource(resources, Utility.getArtResourceForWeatherCondition(weatherId));
                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),desc, Utility.formatTemperature(context, high), Utility.formatTemperature(context, low));

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(resources.getColor(R.color.ColorPrimary))
                                    .setSmallIcon(iconId)
                                    .setLargeIcon(largeIcon)
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    // PARISH_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(PARISH_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }
                cursor.close();
            }
        }
    }
    */


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        CatolicosSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL_debug, SYNC_FLEXTIME_debug);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }



}
