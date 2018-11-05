package com.jpmc.contract;

import com.jpmc.model.Album;

import io.realm.RealmList;

public interface AlbumListContract {

    interface Model{
        interface OnFinishedListener{
            void onSuccess(RealmList<Album> response);
            void onError(String message);
        }

        void getAlbumList(OnFinishedListener onFinishedListener);
    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(RealmList<Album> albumArrayList);

        void onResponseFailure(String message);

    }

    interface Presenter {

        void onDestroy();

        void requestDataFromServer();
    }
}
