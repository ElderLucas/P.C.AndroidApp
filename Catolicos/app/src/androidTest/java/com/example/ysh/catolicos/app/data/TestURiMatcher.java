package com.example.ysh.catolicos.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
  Created by YSH on 31/05/2015.
 */
public class TestURiMatcher extends AndroidTestCase {

    /*
    URi Matches Numbers
    */
    static final int ACTIVITY                   = 100; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/ACTIVITY/
    static final int ACTIVITY_WITH_PARISH       = 101; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ARG1]
    static final int ACTIVITY_WITH_PARISH_DAY   = 102; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ARG1]/[ARG2]
    static final int PARISH                     = 300; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/
    static final int PARISH_WITH_NAME           = 301; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/[ARG1]
    static final int PARISH_WITH_LOCATION       = 302; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH/[ARG1]/[ARG2]


    /*
    Args to compare
    */
    private static final String TESTDAY = "terca";
    private static final String TESTPARISH = "SaoDimas";
    private static final String Lat = "45.3";
    private static final String Long = "-25.3";


    Uri ActivityUri = CatolicosContract.ActivityEntry.CONTENT_URI;
    Uri ActivityParishWithParKeyUri = CatolicosContract.ActivityEntry.build_ActivityParish(TESTPARISH);
    Uri ActivityParishWithParKeyDayUri = CatolicosContract.ActivityEntry.build_ActivityParishWithWeekDay(TESTPARISH, TESTDAY);
    Uri ParishUri = CatolicosContract.ParishEntry.CONTENT_URI;
    Uri ParishWithName = CatolicosContract.ParishEntry.build_ParishWithNameURi(TESTPARISH);
    Uri ParishWithLocationUri = CatolicosContract.ParishEntry.build_ParishWithLocationURi(Lat,Long);


    public void testUriMatcher(){

        UriMatcher testMatcher = CatolicosProvider.buildUriMatcher();

        assertEquals("Error: The Activity URI was matched incorrectly.", testMatcher.match(ActivityUri), ACTIVITY);
        assertEquals("Error: The ActivityParish URI was matched incorrectly.", testMatcher.match(ActivityParishWithParKeyUri), ACTIVITY_WITH_PARISH);
        assertEquals("Error: The ActivityPArish With Day URI was matched incorrectly.", testMatcher.match(ActivityParishWithParKeyDayUri), ACTIVITY_WITH_PARISH_DAY);

        assertEquals("Error: The Activity URI was matched incorrectly.", testMatcher.match(ParishUri), PARISH);
        assertEquals("Error: The Activity URI was matched incorrectly.", testMatcher.match(ParishWithName), PARISH_WITH_NAME);
        assertEquals("Error: The Activity URI was matched incorrectly.", testMatcher.match(ParishWithLocationUri), PARISH_WITH_LOCATION);

    }
}

