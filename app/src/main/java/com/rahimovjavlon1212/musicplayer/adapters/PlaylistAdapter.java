package com.rahimovjavlon1212.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.models.PlaylistData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    private List<PlaylistData> mData;
    private LayoutInflater mInflater;
    private boolean isAddToPlaylist;
    public OnItemClicked onItemClicked;
    public OnCreateClicked onCreateClicked;
    private Context context;


    public PlaylistAdapter(Context context, List<PlaylistData> mData, boolean isAddToPlaylist) {
        this.mData =mData;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.isAddToPlaylist = isAddToPlaylist;
    }
    public void setData(List<PlaylistData> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0 && !isAddToPlaylist) {
            view = mInflater.inflate(R.layout.item_create_playlist, parent, false);
        } else {
            view = mInflater.inflate(R.layout.item_playlist, parent, false);
        }
        return new PlaylistViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size() - 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        if (getItemViewType(position) == 0 && !isAddToPlaylist) {
            holder.itemView.setOnClickListener(v -> {
                if (onCreateClicked != null)
                onCreateClicked.onClick();
            });
        } else {
            holder.playlistName.setText(mData.get(position).getName());
            holder.playlistTracks.setText(mData.get(position).getData().split("#").length-1 + context.getResources().getString(R.string.tracks));
            holder.playlistImage.setImageResource(R.drawable.icon);
            holder.itemView.setOnClickListener(v -> {
                if (onItemClicked!=null)
                onItemClicked.onClick(mData.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClicked {
        void onClick(PlaylistData playlistData);
    }

    public interface OnCreateClicked {
        void onClick();
    }
}

class PlaylistViewHolder extends RecyclerView.ViewHolder {
    TextView playlistName;
    TextView playlistTracks;
    CircleImageView playlistImage;

    PlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        if (getItemViewType() != 0) {
            playlistName = itemView.findViewById(R.id.namePlaylistItemPlaylist);
            playlistTracks = itemView.findViewById(R.id.trackCountPlaylistItemPlaylist);
            playlistImage = itemView.findViewById(R.id.imagePlaylistItemPlaylist);
        }
    }
}
