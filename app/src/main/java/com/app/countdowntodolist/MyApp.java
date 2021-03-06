package com.app.countdowntodolist;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // start initial parse with key
        Parse.initialize(this, "SIzGKpVr1ITnt629TkiQZfxSQE6VA2HddvwbdJQ4", "36WLOG5LBo0AcmIivQRdqUoHbdF1MNJj56zUNyoq");

        //initial facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);

        // initial twitter key
        ParseTwitterUtils.initialize("vwIn54lXsNWD7mYqP0jPZ3uGD", "PvwOPuVKDTYkMgLJxfooinXwvi9BfEo2OZ5e1zPvrXpP8NXpRR");


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}