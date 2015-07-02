package com.example.ysh.catolicos.app.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.ysh.catolicos.app.R;
import com.example.ysh.catolicos.app.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
 Created by YSH on 06/06/2015.
*/
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {

        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());

        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    /*
        Cria alguns valores fake para a activity table
    */
    static ContentValues crearActivityParishValues(String Parish) {

        ContentValues Values = new ContentValues();

        Values.put(CatolicosContract.ActivityEntry.COLUMN_PAR_KEY       ,Parish);
        Values.put(CatolicosContract.ActivityEntry.COLUMN_ID_ATIVIDADE  ,"Missa");
        Values.put(CatolicosContract.ActivityEntry.COLUMN_DIA           ,"sexta");
        Values.put(CatolicosContract.ActivityEntry.COLUMN_DIA_SEMANA    ,"Sexta - Feira");
        Values.put(CatolicosContract.ActivityEntry.COLUMN_HORARIO       ,"19:00");

        return Values;
    }


    /*
        Criando dados para a table PArish
    */
    static ContentValues createParishValues(String Parish) {

        ContentValues ParishValues = new ContentValues();

        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_ID_PAROQUIA, Parish);
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_NOME, "Catedral Sao Dimas");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_REGPASTORAL, "Regiao Pastoral I");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_PHONE, "(12)39911222");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_EMAIL, "catedral@diocese.com.br");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_WEBPAGE, "www.catedral.com.br");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_ADDRESS, "Pca Monsenhor Ascanio Brandao, 01");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_POSTALCODE, "12214000");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_CITY, "SJK");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_LATITUDE, "10.09");
        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_LONGETUDE, "-12.00");

        return ParishValues;
    }

    /*
        Esse metodo valida um conjunto de dados passados em um Content Values e compara com o cursor retornado da consulta ao Banco de dados
     */
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {

            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        TestUtilities Content Observer

        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
    */
    static class TestContentObserver extends ContentObserver {

        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}
