package com.jpmc.appInterface;

import com.jpmc.constants.ApiConstants;
import com.jpmc.model.Album;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET(ApiConstants.GET_ALBUMS)
    Call<RealmList<Album>> getAlbumList();
}
