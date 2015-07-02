package com.example.ysh.catolicos.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
  Created by YSH on 13/06/2015.
 */
public class CatolicosAuthenticatorService extends Service {


    /*
    Ref Link : https://developer.android.com/training/sync-adapters/creating-authenticator.html#CreateAuthenticatorService
    Estudos:
    Bind the Authenticator to the Framework
    - for the sync adapter framework to access your authenticator, you must create a bound Service for it
    */


    /*
       Instance field that stores the authenticator object
       The following snippet shows you how to define the bound Service:
    */
    private CatolicosAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new CatolicosAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
