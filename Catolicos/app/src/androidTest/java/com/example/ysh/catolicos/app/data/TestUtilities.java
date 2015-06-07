package com.example.ysh.catolicos.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.ysh.catolicos.app.R;

import java.util.Map;
import java.util.Set;

/*
 Created by YSH on 06/06/2015.
*/
public class TestUtilities extends AndroidTestCase {

    /*
        Students: Use this to create some default weather values for your database tests.
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
    static ContentValues createParishValues() {

        ContentValues ParishValues = new ContentValues();

        ParishValues.put(CatolicosContract.ParishEntry.COLUMN_ID_PAROQUIA, "SaoDimas");
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

}
