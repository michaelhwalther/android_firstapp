package com.example.miw.firstmiwapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ModelAPIService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private class LocalBinder extends Binder {
        ModelAPIService getAPIServive() {
            return ModelAPIService.this;
        }
    }

    public ModelAPIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //
    // -------------------------------------------------------------------------------------------
    // Public API methods
    //

    public String getSomeData() {
        return "Hello World!!!";
    }
}
