package com.jpmc.utils;

import android.content.Context;

import com.jpmc.constants.AppConstants;
import com.jpmc.model.Album;
import com.jpmc.model.AlbumList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmDatabase {

    private static final String TAG = RealmDatabase.class.getName();

    private static RealmDatabase realmDatabase;
    private static RealmConfiguration realmConfiguration;

    /**
     * Initialization of the Realm Object
     * @param context Activity Context
     */
    private RealmDatabase(Context context){
        Realm.init(context);
        realmConfiguration = new RealmConfiguration.Builder().name(AppConstants.DATABASE_NAME)
                            .schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static RealmDatabase getRealmDatabase(Context context){
        if(realmDatabase == null){
            realmDatabase = new RealmDatabase(context);
            return realmDatabase;
        }
        return realmDatabase;
    }

    /**
     * Add or Update Album Data
     * @param album update the AlbumList table
     */
    public void addAlbumList(AlbumList album) {
        Realm.getInstance(realmConfiguration).beginTransaction();
        Realm.getInstance(realmConfiguration).copyToRealmOrUpdate(album);
        Realm.getInstance(realmConfiguration).commitTransaction();
    }

    /**
     * Get Available Album List in DB
     */
    public RealmList<Album> getAlbumList(){
        RealmList<Album> albumList = new RealmList<>();
        Realm realm = Realm.getInstance(realmConfiguration);
        RealmResults<Album> realmResults = realm.where(Album.class).findAll();
        albumList.addAll(realmResults.subList(0, realmResults.size()));
        return albumList;
    }
}
