package com.example.ysh.catolicos.app.data;

import android.net.Uri;
import android.test.AndroidTestCase;


/*
  Created by YSH on 31/05/2015.
  Testar o contract
 */
public class TestCatolicosContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    static final String TEST_ACTIVITY_PARISH = "SaoDimas";
    public void testBuildActivityParishURi() {

        Uri ActivityParishUri = CatolicosContract.ActivityEntry.build_ActivityParish(TEST_ACTIVITY_PARISH);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildActivityParish in " + "CatolicosContract.", ActivityParishUri);
        assertEquals("Error: Activity Parish ID not properly appended to the end of the Uri", TEST_ACTIVITY_PARISH, ActivityParishUri.getLastPathSegment());
        assertEquals("Error: Activity With Parish Name Uri doesn't match our expected result", ActivityParishUri.toString(), "content://com.example.ysh.catolicos.app/atividade/SaoDimas");
    }


}
