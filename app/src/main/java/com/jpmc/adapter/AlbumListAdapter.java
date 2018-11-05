package com.jpmc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmc.R;
import com.jpmc.model.Album;
import com.jpmc.view.AlbumListActivity;

import io.realm.RealmList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumListViewHolder> {

    private String TAG = AlbumListAdapter.class.getName();

    private RealmList<Album> mAlbumList;
    private AlbumListActivity albumListActivity;

    public AlbumListAdapter(AlbumListActivity albumListActivity, RealmList<Album> albumList){
        this.albumListActivity = albumListActivity;
        mAlbumList = albumList;
    }
    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.album_list_item, parent, false);
        return new AlbumListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, final int position) {
        holder.albumTitle.setText(mAlbumList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumListActivity.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    /**
     * ViewHolder Items for AlbumList Inflated to RecyclerView
     */
    class AlbumListViewHolder extends RecyclerView.ViewHolder{
        private TextView albumTitle;
        AlbumListViewHolder(View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.album_title);
        }
    }
}
