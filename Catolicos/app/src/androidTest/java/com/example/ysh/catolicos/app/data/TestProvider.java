package com.example.ysh.catolicos.app.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import com.example.ysh.catolicos.app.data.CatolicosContract.ActivityEntry;
import com.example.ysh.catolicos.app.data.CatolicosContract.ParishEntry;

/*
  Created by YSH on 02/06/2015.
 */
public class TestProvider extends AndroidTestCase {

    CatolicosProvider mProvider = new CatolicosProvider();

    /*
    Para testar o Get_type com URi da "ParishEntry"
     */
    Uri ParishURi               = CatolicosContract.ParishEntry.buildParishUri();
    Uri ParishWithNameURi       = CatolicosContract.ParishEntry.build_ParishWithNameURi("SaoDimas");
    Uri ParishWithLocationURi   = CatolicosContract.ParishEntry.build_ParishWithLocationURi("10.0", "12.0");

    Uri ActivityURi             = CatolicosContract.ActivityEntry.buildActivityUri();
    Uri ActivityWithParishURi   = CatolicosContract.ActivityEntry.build_ActivityParish("SaoDimas");
    Uri ActivityWithWeekDay     = CatolicosContract.ActivityEntry.build_ActivityParishWithWeekDay("Catedral", "terca");

    /*
    public static final String CONTENT_AUTHORITY = "com.example.ysh.catolicos.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ACTIVITY = "atividade";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTIVITY).build();
    */

    public void testGetType() {

        /*
        A estrutura desse teste é basicamente verificar se eh diretorio ou item.
        Caso o retorno seja zerou ou um cursor com mais de um item, eh do tipo "DIR (Directory)"
        Caso o retorno seja de um item apenas, é do tipo "ITEM"
        Esse Teste Verifica a estrutura da URi
         */
        String type = mContext.getContentResolver().getType(ParishURi);
        assertEquals("Error: the ParishEntry CONTENT_URI should return ParishEntry.CONTENT_TYPE", ParishEntry.CONTENT_TYPE, type);

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

}
