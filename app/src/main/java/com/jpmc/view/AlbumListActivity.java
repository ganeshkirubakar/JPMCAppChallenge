package com.jpmc.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpmc.R;
import com.jpmc.adapter.AlbumListAdapter;
import com.jpmc.appInterface.AlbumListItemClickListener;
import com.jpmc.constants.AppConstants;
import com.jpmc.contract.AlbumListContract;
import com.jpmc.model.Album;
import com.jpmc.model.AlbumList;
import com.jpmc.presenter.AlbumListPresenter;
import com.jpmc.utils.DialogUtils;
import com.jpmc.utils.RealmDatabase;

import io.realm.RealmList;

public class AlbumListActivity extends AppCompatActivity implements AlbumListContract.View, AlbumListItemClickListener {

    private final String TAG = AlbumListActivity.class.getName();

    private RecyclerView mAlbumView;
    private ProgressBar mProgressBar;
    private TextView mUnavailableText;
    public AlbumListPresenter albumListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlbumView  = findViewById(R.id.album_list);
        mProgressBar = findViewById(R.id.album_progress);
        mUnavailableText = findViewById(R.id.items_unavailable);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAlbumView.setLayoutManager(mLayoutManager);

        albumListPresenter = new AlbumListPresenter(this);
        getAlbumList();
    }

    /**
     * Get Album List from Api
     */
    private void getAlbumList(){
        if(checkNetworkConnection()) {
            albumListPresenter.requestDataFromServer();
        }else{
            mProgressBar.setVisibility(View.GONE);
            //setAlbumListAdapter(AppDatabase.getInstance(this).getAlbumList());
            setAlbumListAdapter(RealmDatabase.getRealmDatabase(this).getAlbumList());
        }
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(RealmList<Album> albumArrayList) {
        if(albumArrayList != null
                && albumArrayList.size() > 0){
            AlbumList albumList = new AlbumList();
            albumList.setRealmList(albumArrayList);
            RealmDatabase.getRealmDatabase(this).addAlbumList(albumList);
            //AppDatabase.getInstance(this).addAlbum(albumArrayList);
        }
        setAlbumListAdapter(albumArrayList);
    }

    @Override
    public void onResponseFailure(String message) {
        mProgressBar.setVisibility(View.GONE);
        mUnavailableText.setVisibility(View.VISIBLE);
        DialogUtils.getInstance().showAlertDialog(AlbumListActivity.this, message);
    }

    /**
     * Set Album List based on Availability Of network
     * @param albumArrayList Retrieved from DB if offline, else from API call
     */
    private void setAlbumListAdapter(RealmList<Album> albumArrayList){
        if(albumArrayList != null
                && albumArrayList.size() > 0) {
            mUnavailableText.setVisibility(View.GONE);
            RecyclerView.Adapter mRecyclerAdapter = new AlbumListAdapter(this, albumArrayList);
            mAlbumView.setAdapter(mRecyclerAdapter);
        }else{
            mUnavailableText.setVisibility(View.VISIBLE);
            DialogUtils.getInstance().showAlertDialog(AlbumListActivity.this, AppConstants.NETWORK_UNAVAILABLE);
        }
    }

    /**
     * Check Network Availability
     * @return returns to availability of network to load data
     */
    public boolean checkNetworkConnection(){
        boolean isNetworkAvailable;
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        isNetworkAvailable = networkInfo != null && networkInfo.isConnected();
        return isNetworkAvailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        albumListPresenter.onDestroy();
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "position :"+position);
    }

}
