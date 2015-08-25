package com.example.ysh.catolicos.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
    Created by YSH on 13/06/2015.
 */

/*
Create a bound Service that passes a special Android binder object from the sync adapter component to the framework.
 */
public class CatolicosSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static CatolicosSyncAdapter sCatolicosSyncAdapter = null;

    @Override
    public void onCreate() {
        //Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sCatolicosSyncAdapter == null) {
                sCatolicosSyncAdapter = new CatolicosSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sCatolicosSyncAdapter.getSyncAdapterBinder();

    }
}