package com.example.ysh.catolicos.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.text.Html;
import android.util.Log;

import com.example.ysh.catolicos.app.sync.CatolicosSyncAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class TestJSON extends InstrumentationTestCase {

    /*

    public TestJSON(String name) {
        super();
    }


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public final void testSomething() {
        fail("Not implemented yet");
    }

*/
    public void testActivityAndParishTables() throws Exception{
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}