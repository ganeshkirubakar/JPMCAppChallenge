package com.jpmc.presenter;

import com.jpmc.service.AlbumListService;
import com.jpmc.contract.AlbumListContract;
import com.jpmc.model.Album;

import java.util.Collections;
import java.util.Comparator;

import io.realm.RealmList;

public class AlbumListPresenter implements AlbumListContract.Presenter, AlbumListContract.Model.OnFinishedListener {

    private AlbumListContract.View albumListView;

    private AlbumListContract.Model albumListModel;

    public AlbumListPresenter(AlbumListContract.View albumListView) {
        this.albumListView = albumListView;
        albumListModel = new AlbumListService();
    }

    @Override
    public void onSuccess(RealmList<Album> response) {
        sortByTitle(response);
        albumListView.setDataToRecyclerView(response);
        if (albumListView != null) {
            albumListView.hideProgress();
        }
    }

    @Override
    public void onError(String message) {
        albumListView.onResponseFailure(message);
        if (albumListView != null) {
            albumListView.hideProgress();
        }
    }

    @Override
    public void onDestroy() {
        this.albumListView = null;
    }

    @Override
    public void requestDataFromServer() {
        if (albumListView != null) {
            albumListView.showProgress();
        }
        albumListModel.getAlbumList(this);
    }

    /**
     * Sort ArrayList by Title from response
     * @param response Api retrieved response
     */
    private void sortByTitle(RealmList<Album> response){
        Collections.sort(response, new AlbumListComparator());
    }

    private class AlbumListComparator implements Comparator<Album> {
        @Override
        public int compare(Album o1, Album o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

}
