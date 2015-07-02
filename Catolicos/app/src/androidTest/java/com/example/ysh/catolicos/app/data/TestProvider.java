package com.example.ysh.catolicos.app.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import com.example.ysh.catolicos.app.data.CatolicosContract.ActivityEntry;
import com.example.ysh.catolicos.app.data.CatolicosContract.ParishEntry;

/*
  Created by YSH on 02/06/2015.
 */
public class TestProvider extends AndroidTestCase {

    CatolicosProvider mProvider = new CatolicosProvider();

    long simulated_id = 1;

    /*
    Para testar o Get_type com URi da "ParishEntry"
     */
    Uri ParishURi               = CatolicosContract.ParishEntry.buildParishUri(simulated_id);
    Uri ParishWithNameURi       = CatolicosContract.ParishEntry.build_ParishWithNameURi("SaoDimas");
    Uri ParishWithLocationURi   = CatolicosContract.ParishEntry.build_ParishWithLocationURi("10.0", "12.0");

    Uri ActivityURi             = CatolicosContract.ActivityEntry.buildActivityUri(simulated_id);
    Uri ActivityWithParishURi   = CatolicosContract.ActivityEntry.build_ActivityParish("SaoDimas");
    Uri ActivityWithWeekDay     = CatolicosContract.ActivityEntry.build_ActivityParishWithWeekDay("Catedral", "terca");




    /*
           This helper function deletes all records from both database tables using the ContentProvider.
           It also queries the ContentProvider to make sure that the database has been successfully
           deleted, so it cannot be used until the Query and Delete functions have been written
           in the ContentProvider.

           Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
           the delete functionality in the ContentProvider.
         */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                ParishEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ActivityEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                ParishEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from ParishEntry table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ActivityEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from ActivityEntry table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
        This helper function deletes all records from both database tables using the database
        functions only.  This is designed to be used to reset the state of the database until the
        delete functionality is available in the ContentProvider.
    */
    public void deleteAllRecordsFromDB() {
        CatolicosDbHelper dbHelper = new CatolicosDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(ActivityEntry.TABLE_NAME, null, null);
        db.delete(ParishEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    /*
        Since we want each test to start with a clean slate, run deleteAllRecords
        in setUp (called by the test runner before each test).
    */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        Deletando informações do DB através do ContentProvider
    */
    public void testDeleteRecords() {

        testInsertReadProvider();

        TestUtilities.TestContentObserver ParishObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ParishEntry.CONTENT_URI, true, ParishObserver);

       // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver ActivityObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ActivityEntry.CONTENT_URI, true, ActivityObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        ParishObserver.waitForNotificationOrFail();
        ActivityObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(ParishObserver);
        mContext.getContentResolver().unregisterContentObserver(ActivityObserver);

    }


    /*
        Testado ok
    */
    public void testGetType() {

        /*
        A estrutura desse teste é basicamente verificar se eh diretorio ou item.
        Caso o retorno seja zerou ou um cursor com mais de um item, eh do tipo "DIR (Directory)"
        Caso o retorno seja de um item apenas, é do tipo "ITEM"
        Esse Teste Verifica a estrutura da URi
         */
        String type = mContext.getContentResolver().getType(ParishURi);
        assertEquals("Error: the ParishEntry CONTENT_URI should return ParishEntry.CONTENT_TYPE", ParishEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(ParishWithNameURi);
        assertEquals("Error: the ParishEntry CONTENT_URI should return ParishEntry.CONTENT_ITEM_TYPE", ParishEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(ParishWithLocationURi);
        assertEquals("Error: the ParishEntry CONTENT_URI should return ParishEntry.CONTENT_ITEM_TYPE", ParishEntry.CONTENT_ITEM_TYPE, type);

        /*
        Aqui se iniciam todas as URIs para teste dos tipos da Activity
        */
        type = mContext.getContentResolver().getType(ActivityURi);
        assertEquals("Error: the ActivityEntry CONTENT_URI should return ActivityEntry.CONTENT_TYPE", ActivityEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ActivityWithParishURi);
        assertEquals("Error: the ActivityEntry CONTENT_URI should return ActivityEntry.CONTENT_TYPE", ActivityEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ActivityWithWeekDay);
        assertEquals("Error: the ActivityEntry CONTENT_URI should return ActivityEntry.CONTENT_TYPE", ActivityEntry.CONTENT_TYPE, type);
    }


    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createParishValues("Par1");
        ContentValues testValues1 = TestUtilities.createParishValues("Par2");
        ContentValues testValues2 = TestUtilities.createParishValues("Par3");
        ContentValues testValues3 = TestUtilities.createParishValues("Par4");

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(ParishEntry.CONTENT_URI, true, tco);

        Uri ParishUri = mContext.getContentResolver().insert(ParishEntry.CONTENT_URI, testValues);
        Uri ParishUri1 = mContext.getContentResolver().insert(ParishEntry.CONTENT_URI, testValues1);
        Uri ParishUri2 = mContext.getContentResolver().insert(ParishEntry.CONTENT_URI, testValues2);
        Uri ParishUri3 = mContext.getContentResolver().insert(ParishEntry.CONTENT_URI, testValues3);

        // Did our content observer get called?  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(tco);

        long ParishRowId = ContentUris.parseId(ParishUri);

        // Verify we got a row back.
        assertTrue(ParishRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ParishEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);


        /*
            Agora, vamos inserir alguns dados na tabela Activity Parish
        */
        ContentValues ActivityValues = TestUtilities.crearActivityParishValues("SaoDimas");
        // The TestContentObserver is a one-shot class

        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(ActivityEntry.CONTENT_URI, true, tco);

        Uri ActivityInsertUri = mContext.getContentResolver().insert(ActivityEntry.CONTENT_URI, ActivityValues);

        assertTrue(ActivityInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor ActivityParishCursor = mContext.getContentResolver().query(
                ActivityEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.", ActivityParishCursor, ActivityValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        ActivityValues.putAll(testValues);

        ActivityParishCursor = mContext.getContentResolver().query(
                ActivityEntry.build_ActivityParish("SaoDimas"),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data.",
                ActivityParishCursor, ActivityValues);
    }


    /*
        Bulk Inserted  **NAO TESTADO**
    */
    /*
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
                cursor, testValues);

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues(locationRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, weatherObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                WeatherEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }*/

}
