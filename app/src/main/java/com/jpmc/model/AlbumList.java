package com.jpmc.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlbumList extends RealmObject {
    @PrimaryKey
    private int id;
    private RealmList<Album> realmList;

    public RealmList<Album> getRealmList() {
        return realmList;
    }

    public void setRealmList(RealmList<Album> realmList) {
        this.realmList = realmList;
    }
}
