package com.example.ysh.catolicos.app.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
  Created by YSH on 31/05/2015.
 */
public class TestURiMatcher extends AndroidTestCase {

    static final int ACTIVITY                   = 100; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/ACTIVITY
    static final int ACTIVITY_WITH_PARISH       = 101; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ID_PAROQUIA QUERY]/[DAY QUERY]
    static final int ACTIVITY_WITH_PARISH_DAY   = 102; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH_ACTIVITY/[ID_PAROQUIA QUERY]/[DAY QUERY]

    static final int PARISH                     = 300; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH
    static final int PARISH_WITH_LOCATION       = 301; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH
    static final int PARISH_WITH_REGPASTORAL    = 302; //CONTENT://COM.EXAMPLE.YSH.CATOLICOS.APP/PARISH


    private static final String TESTDAY = "terca";
    static final String TESTPARISH = "SaoDimas";



    Uri ActivityParishWithParKeyUri = CatolicosContract.ActivityEntry.build_ActivityParish(TESTPARISH);
    Uri ActivityParishWithParKeyDayUri = CatolicosContract.ActivityEntry.build_ActivityParishWithWeekDay(TESTPARISH, TESTDAY);

    public void testUriMatcher(){

        UriMatcher testMatcher = CatolicosProvider.buildUriMatcher();


        assertEquals("Error: The ActivityParish URI was matched incorrectly.", testMatcher.match(ActivityParishWithParKeyUri), CatolicosProvider.ACTIVITY_WITH_PARISH);
        assertEquals("Error: The ActivityPArish With Day URI was matched incorrectly.", testMatcher.match(ActivityParishWithParKeyDayUri), CatolicosProvider.ACTIVITY_WITH_PARISH_DAY);

        //assertEquals("Error: The WEATHER WITH LOCATION AND DATE URI was matched incorrectly.", testMatcher.match(TEST_WEATHER_WITH_LOCATION_AND_DATE_DIR), WeatherProvider.WEATHER_WITH_LOCATION_AND_DATE);

       // assertEquals("Error: The LOCATION URI was matched incorrectly.", testMatcher.match(TEST_LOCATION_DIR), WeatherProvider.LOCATION);

    }
}

