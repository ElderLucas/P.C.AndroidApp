package com.example.ysh.catolicos.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/*
 Created by YSH on 06/06/2015.
 */
public class TestDB  extends AndroidTestCase {

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(CatolicosDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }


    /*
    Criando o Data Base
    */

    public void testCreateDb() throws Throwable {

        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();

        tableNameHashSet.add(CatolicosContract.ParishEntry.TABLE_NAME);
        tableNameHashSet.add(CatolicosContract.ActivityEntry.TABLE_NAME);

        mContext.deleteDatabase(CatolicosDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new CatolicosDbHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        /*
            have we created the tables we want?
        */
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        String first  = c.getString(0);

        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without both the location entry and weather entry tables", tableNameHashSet.isEmpty());

        /*
            now, do our tables contain the correct columns?
        */
        c = db.rawQuery("PRAGMA table_info(" + CatolicosContract.ActivityEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        final HashSet<String> ActivityColumnHashSet = new HashSet<String>();

        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry._ID);
        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry.COLUMN_PAR_KEY);
        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry.COLUMN_ID_ATIVIDADE);
        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry.COLUMN_DIA);
        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry.COLUMN_DIA_SEMANA);
        ActivityColumnHashSet.add(CatolicosContract.ActivityEntry.COLUMN_HORARIO);

        int columnNameIndex;

        columnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnNameIndex);
            ActivityColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location entry columns
        assertTrue("Error: The database doesn't contain all of the required Activitys entry columns",ActivityColumnHashSet.isEmpty());

        db.close();

    }


    /*
        Teste das tabelas de Atividades e das Paroquias
    */
    public void testActivityAndParishTables() {

        SQLiteDatabase myTestDB = new CatolicosDbHelper(mContext).getWritableDatabase();

        /*
            Operacoes com a tabela ActivityTable
        */
        ContentValues myWritebleData = TestUtilities.crearActivityParishValues("SaoDimas");

        //OBS : Eu so estou usando a tabela de activity, sem o INNER com a Parish....
        long ActivityWithParishCursorRowID = myTestDB.insert(CatolicosContract.ActivityEntry.TABLE_NAME, null, myWritebleData);

        assertTrue(ActivityWithParishCursorRowID != -1);

        Cursor myTestDBCursor = myTestDB.query(CatolicosContract.ActivityEntry.TABLE_NAME, null, null, null, null, null, null, null);

        boolean returnCursorMoviment = myTestDBCursor.moveToFirst();

        assertTrue("Error: Cursor is empty..", returnCursorMoviment);

        TestUtilities.validateCurrentRecord("MyTestDB Error", myTestDBCursor, myWritebleData);

        assertFalse("Error: More than one record returned from location query", myTestDBCursor.moveToNext());

        /*
            New Tests.... PARISH Table Operacoes com a tabela ParishTable
        */
        ContentValues myParishWritebleData = TestUtilities.createParishValues("Saodimas");

        long ParishCursorRowID = myTestDB.insert(CatolicosContract.ParishEntry.TABLE_NAME, null, myParishWritebleData);

        assertTrue(ParishCursorRowID != -1);

        Cursor myParishTestDBCursor = myTestDB.query(CatolicosContract.ParishEntry.TABLE_NAME, null,null,null,null,null,null,null);

        returnCursorMoviment = myParishTestDBCursor.moveToFirst();

        assertTrue("Error : Cursor is Empty... ", returnCursorMoviment);

        TestUtilities.validateCurrentRecord("My TestDB Error", myParishTestDBCursor, myParishWritebleData);

        /*
        Verifica se mais de um valor foi retornado, o que nao se espera neste mommento, ja que inserimos apenas uma linha
        */
        assertFalse("Error: Mais de uma linha foi retornado nesse Momento", myParishTestDBCursor.moveToNext());

    }

}


