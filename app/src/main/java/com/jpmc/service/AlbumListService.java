package com.jpmc.service;

import android.support.annotation.NonNull;

import com.jpmc.appInterface.ApiInterface;
import com.jpmc.constants.AppConstants;
import com.jpmc.contract.AlbumListContract;
import com.jpmc.model.Album;
import com.jpmc.network.RestAdapter;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumListService implements AlbumListContract.Model {

    private String TAG = AlbumListService.class.getName();

    @Override
    public void getAlbumList(final OnFinishedListener onFinishedListener) {

        ApiInterface albumListApi = init();

        Call<RealmList<Album>> call = albumListApi.getAlbumList();
        call.enqueue(new Callback<RealmList<Album>>() {
            @Override
            public void onResponse(@NonNull Call<RealmList<Album>> call, @NonNull Response<RealmList<Album>> response) {
                if (response.body() != null) {
                    onFinishedListener.onSuccess(response.body());
                } else {
                    onFinishedListener.onError(AppConstants.APP_GENERIC_ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RealmList<Album>> call, @NonNull Throwable t) {
                onFinishedListener.onError(t.getMessage());
            }
        });
    }

    /**
     * Initialize AlbumList Api interface
     *
     * @return AlbumList Api Interface
     */
    private ApiInterface init() {
        return RestAdapter.getClient().create(ApiInterface.class);
    }
}
