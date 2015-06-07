package com.example.ysh.catolicos.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.ysh.catolicos.app.data.CatolicosContract.ActivityEntry;
import com.example.ysh.catolicos.app.data.CatolicosContract.ParishEntry;

/*
 * Created by YSH on 27/05/2015.
 */
public class CatolicosDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "Catolicos.db";

    public CatolicosDbHelper(Context context)
    {
        //Create a helper object to create, open, and/or manage a database.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String LOG_TAG = CatolicosDbHelper.class.getSimpleName();

        final String SQL_CREATE_PARISH_TABLE =
                "CREATE TABLE " +
                ParishEntry.TABLE_NAME + " (" +
                ParishEntry.COLUMN_ID_PAROQUIA  + " TEXT UNIQUE NOT NULL, " +        // Uniquely identified each rows/records in a database table
                ParishEntry.COLUMN_NOME         + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_REGPASTORAL  + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_PHONE        + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_EMAIL        + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_WEBPAGE      + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_ADDRESS      + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_POSTALCODE   + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_CITY         + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_LATITUDE     + " TEXT NOT NULL, " +
                ParishEntry.COLUMN_LONGETUDE    + " TEXT NOT NULL  " + " );";


        final String SQL_CREATE_ACTIVITY_TABLE =

                "CREATE TABLE " +
                ActivityEntry.TABLE_NAME          + " (" +                                          //PAra que seja possivel usar o cursor adapter juntamente com o listview widget, é necessário essa coluna
                ActivityEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT," +         //The Cursor must include a column named "_id" or this class will not work [http://developer.android.com/reference]
                ActivityEntry.COLUMN_PAR_KEY      + " TEXT NOT NULL,  " +
                ActivityEntry.COLUMN_ID_ATIVIDADE + " TEXT NOT NULL,  " +
                ActivityEntry.COLUMN_DIA          + " TEXT NOT NULL,  " +
                ActivityEntry.COLUMN_DIA_SEMANA   + " TEXT NOT NULL,  " +
                ActivityEntry.COLUMN_HORARIO      + " TEXT NOT NULL,  " +

                // Set up the Parish_id column as a foreign key to location table.
                " FOREIGN KEY (" + ActivityEntry.COLUMN_PAR_KEY + ") REFERENCES " + ParishEntry.TABLE_NAME + " (" + ParishEntry.COLUMN_ID_PAROQUIA + "));";

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                //" UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
                //WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        Log.v(LOG_TAG, "Data Base Create Table PArameters : Parish     :\n" + SQL_CREATE_PARISH_TABLE);
        Log.v(LOG_TAG, "Data Base Create Table Parameters : Activity   :\n" + SQL_CREATE_ACTIVITY_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_PARISH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACTIVITY_TABLE);
    }


    //Se eu mudar a versao da minha database, esse método eh chamado.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParishEntry.TABLE_NAME);  //
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParishActivityEntry.TABLE_NAME);   // Deleta os dois DB
        //onCreate(sqLiteDatabase); //Cria o banco Novamente.
    }

}
