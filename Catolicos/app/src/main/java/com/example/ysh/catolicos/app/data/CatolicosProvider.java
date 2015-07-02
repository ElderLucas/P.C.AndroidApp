package com.example.ysh.catolicos.app.data;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/*
  Created by YSH on 27/05/2015.
  www.ysh.com.br
  Elder Lucas de Oliveira Santos
 */
public class CatolicosProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private CatolicosDbHelper mOpenHelper;


    /*
        O Motivo pelo qual se faz a consulta com numero maiores que dezena, e para agilizar a identificacao
        da ordem da URi que esta entrado.
     */
    static final int ACTIVITY                   = 100; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/ACTIVITY/
    static final int ACTIVITY_WITH_PARISH       = 101; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ARG1]
    static final int ACTIVITY_WITH_PARISH_DAY   = 102; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ARG1]/[ARG2]

    static final int PARISH                     = 300; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/
    static final int PARISH_WITH_NAME           = 301; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/[ARG1]
    static final int PARISH_WITH_LOCATION       = 302; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/[ARG1]/[ARG2]


    /*
    My Query Statements
    */
    private static final SQLiteQueryBuilder sParishQueryBuilder;
    static {
        sParishQueryBuilder = new SQLiteQueryBuilder();
        sParishQueryBuilder.setTables(CatolicosContract.ParishEntry.TABLE_NAME);
    }

    /*
    My Query Statements
    */
    private static final SQLiteQueryBuilder sActivityQueryBuilder;
    static {
        sActivityQueryBuilder = new SQLiteQueryBuilder();
        sActivityQueryBuilder.setTables(CatolicosContract.ActivityEntry.TABLE_NAME);
    }

    /*
    Build a query to return Parish Activities in a spacifica Day Week. All Dayweek activities
    */
    private static final SQLiteQueryBuilder sActivityWithParishQueryBuilder;
    static{
        sActivityWithParishQueryBuilder = new SQLiteQueryBuilder();
        sActivityWithParishQueryBuilder.setTables(
                CatolicosContract.ActivityEntry.TABLE_NAME + " INNER JOIN " +
                        CatolicosContract.ParishEntry.TABLE_NAME +
                        " ON " + CatolicosContract.ActivityEntry.TABLE_NAME +
                        "." + CatolicosContract.ActivityEntry.COLUMN_PAR_KEY +
                        " = " + CatolicosContract.ParishEntry.TABLE_NAME +
                        "." + CatolicosContract.ParishEntry.COLUMN_ID_PAROQUIA);
    }



    /*
    Selection Parts Stantments String to query a specific parish Activity
    */
    private static final String sActivityByPARKEYSelection =
            CatolicosContract.ActivityEntry.TABLE_NAME + "." + CatolicosContract.ActivityEntry.COLUMN_PAR_KEY + " = ? ";

    /*
    String para compor uma query de construir uma tabela Parish
    */
    private static final String sActivityByPARKEYAndDaySelection =
            CatolicosContract.ActivityEntry.TABLE_NAME + "." + CatolicosContract.ActivityEntry.COLUMN_PAR_KEY + " = ? AND " + CatolicosContract.ActivityEntry.COLUMN_DIA + " = ? ";

    /*
    String para construir uma query para a tabela Parish
    */
    private static final String sParishByParishNameSelection =
            CatolicosContract.ParishEntry.TABLE_NAME + "." + CatolicosContract.ParishEntry.COLUMN_ID_PAROQUIA + " = ? ";

    /*
    Query Methods
    */
    private Cursor getActivityWithParishByPAR_KEY(Uri uri, String[] projection, String sortOrder){
        /*
        Aqui pode-se fazer uma condicional para determinar se os argumentos estao dentro do esperado
        */
        String Parish = CatolicosContract.ActivityEntry.getParishFromUri(uri);
        String[] selectionArgs = new String[]{Parish};
        String selection = sActivityByPARKEYSelection;


        return sActivityWithParishQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getActivityWithParishByPAR_KEYandDAY(Uri uri, String[] projection, String sortOrder){

        String Parish = CatolicosContract.ActivityEntry.getParishFromUri(uri);
        String Day = CatolicosContract.ActivityEntry.getDayFromUri(uri);

        /*
            Aqui pode-se fazer uma condicional para determinar se os argumentos estao dentro do esperado
        */
        String[] selectionArgs = new String[]{Parish, Day};
        String selection = sActivityByPARKEYAndDaySelection;


        return sActivityWithParishQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getParishCursor(Uri uri, String[] projection, String sortOrder) {
        return sParishQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getActivityCursor(Uri uri, String[] projection, String sortOrder) {
        return sActivityQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    /*
    This UriMatcher will match each URI to the Parish, Parish_With_ActivityDay and Activity
    */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        // -----------------------------------------------------------------------------------------
        //Faz-se isso prq nao queremos que compare com nada que nao seja por nos definido.
        final UriMatcher myURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        //------------------------------------------------------------------------------------------
        final String authority = CatolicosContract.CONTENT_AUTHORITY;

        ////////////////////////////////////////////////////////////////////////////////////////////
        //                           |---------------------------------------------------------| "com.example.android.sunshine.app"
        //                           |                              |--------------------------| "weather"
        //                           |                              |                       |--| Esse eh o codigo que sera retornado qndo chegar uma URI desse tipo.
        //                           |                              |                       |
        //                   |-- Authority -| |-------- Path -------------| |--Code--|
        //myURIMatcher.addURI(authority,      WeatherContract.PATH_WEATHER,   PARISH);

        /*
            eh Necessario somente as duas comparacoes, porque ou eu pesquiso a paroquia ou a atividade de uma paroquia
        */


        /*
        As possiveis formacoes de uma URi para acessar a table Parish
        */
        myURIMatcher.addURI(authority, CatolicosContract.PATH_PARISH,PARISH);
        myURIMatcher.addURI(authority, CatolicosContract.PATH_PARISH +"/*",PARISH_WITH_NAME);
        myURIMatcher.addURI(authority, CatolicosContract.PATH_PARISH +"/*/*",PARISH_WITH_LOCATION);

        /*
        As possiveis formacoes de uma URi para acessar a table Actitvity
        */
        myURIMatcher.addURI(authority, CatolicosContract.PATH_ACTIVITY, ACTIVITY);
        myURIMatcher.addURI(authority, CatolicosContract.PATH_ACTIVITY + "/*", ACTIVITY_WITH_PARISH);
        myURIMatcher.addURI(authority, CatolicosContract.PATH_ACTIVITY +"/*/*", ACTIVITY_WITH_PARISH_DAY);

        /*
        todo Colocar mais matchs para comparar quando entrar
        */
        return myURIMatcher;
    }

    /*
    We just create a new WeatherDbHelper for later use here.
    */
    @Override
    public boolean onCreate() {
        /*
        DB HELPER Contem funoees que ajudarao na manipulacao do DB
        Create a helper object to create, open, and/or manage a database.
        */
        mOpenHelper = new CatolicosDbHelper(getContext());    // getContext () -> PAssa como parametro o contexo da VIEW onde estamos rodando
        return true;
    }

    @Override
    public String getType(Uri uri) {
        //Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ACTIVITY:
                return CatolicosContract.ActivityEntry.CONTENT_TYPE;    //  containing a Cursor of zero or more items.

            case ACTIVITY_WITH_PARISH_DAY:
                return CatolicosContract.ActivityEntry.CONTENT_TYPE;    //  containing a Cursor of zero or more items.

            case ACTIVITY_WITH_PARISH:
                return CatolicosContract.ActivityEntry.CONTENT_TYPE;    //  containing a Cursor of zero or more items.

            case PARISH:
                return CatolicosContract.ParishEntry.CONTENT_TYPE;      //  Aqui retorna uma selecao de intens....

            case PARISH_WITH_NAME:
                return CatolicosContract.ParishEntry.CONTENT_ITEM_TYPE; //  Aqui retorna um item

            case PARISH_WITH_LOCATION:
                return CatolicosContract.ParishEntry.CONTENT_ITEM_TYPE; //  Aqui retorna um item

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /*
    Here's the switch statement that, given a URI, will determine what kind of request it is, and query the database accordingly.
    */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            /*
                Table Activity+Parish By ParKey
             */
            case ACTIVITY_WITH_PARISH:
            {
                retCursor = getActivityWithParishByPAR_KEY(uri, projection, sortOrder);
                break;
            }

            /*
                Table Activity+Parish By ParKey an Day
            */
            case ACTIVITY_WITH_PARISH_DAY: {
                retCursor = getActivityWithParishByPAR_KEYandDAY(uri, projection, sortOrder);
                break;
            }

            /*
                Table Parish without Selection
            */
            case PARISH: {
                retCursor = getParishCursor(uri, projection, sortOrder);
                break;
            }

            /*
                Table Parish without Selection
            */
            case ACTIVITY: {
                retCursor = getActivityCursor(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
    */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PARISH: {
                long _id = db.insert(CatolicosContract.ParishEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = CatolicosContract.ParishEntry.buildParishUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case  ACTIVITY: {
                long _id = db.insert(CatolicosContract.ActivityEntry.TABLE_NAME, null,values);
                if ( _id > 0 )
                    returnUri = CatolicosContract.ActivityEntry.buildActivityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        int NumberOfRowAffected = 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);

        /*
            A null value deletes all rows.  In my implementation of this, I only notified
            the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
            is null. Oh, and you should notify the listeners here.
        */
        if (null == selection )
            selection = "1";

        switch (match) {
            case PARISH: {
                NumberOfRowAffected = db.delete(CatolicosContract.ParishEntry.TABLE_NAME, selection,selectionArgs);
                break;
            }

            case  ACTIVITY: {
                NumberOfRowAffected = db.delete(CatolicosContract.ActivityEntry.TABLE_NAME, selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(NumberOfRowAffected != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        db.close();
        // Student: return the actual rows deleted
        return NumberOfRowAffected;
    }

    /*
    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
        }
    }
    */

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.

        // Student: Start by getting a writable database
        int NumberOfRowImpacted = 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        switch (match) {
            case PARISH: {
                NumberOfRowImpacted = db.update(CatolicosContract.ParishEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            case ACTIVITY: {
                NumberOfRowImpacted = db.update(CatolicosContract.ActivityEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (NumberOfRowImpacted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        db.close();

        // Student: return the actual rows deleted
        return NumberOfRowImpacted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACTIVITY:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CatolicosContract.ActivityEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }


}
