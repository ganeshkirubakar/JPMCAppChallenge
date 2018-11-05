package com.jpmc;

import android.app.Application;

import io.realm.Realm;

public class AlbumListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
