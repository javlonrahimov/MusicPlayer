package com.rahimovjavlon1212.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LibraryAdapter extends RecyclerView.Adapter<MusicViewHolder> {
    private List<MusicData> mData;
    private LayoutInflater mInflater;
    private Context context;
    public OnItemClicked onItemClicked;

    public LibraryAdapter(Context context, List<MusicData> mData) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, final int position) {
            holder.musicTitle.setText(mData.get(position).getTitle());
            holder.musicArtist.setText(mData.get(position).getArtist());
            holder.musicImage.setImageResource(mData.get(position).getImagePath());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClicked != null) {
                        onItemClicked.onClick(mData.get(position).getMusicPath());
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClicked{
        void onClick(String path);
    }
}

class MusicViewHolder extends RecyclerView.ViewHolder {

    TextView musicTitle;
    TextView musicArtist;
    CircleImageView musicImage;

    MusicViewHolder(@NonNull View itemView) {
        super(itemView);
        musicArtist = itemView.findViewById(R.id.artistItemMusic);
        musicImage = itemView.findViewById(R.id.imageItemMusic);
        musicTitle = itemView.findViewById(R.id.titleItemMusic);
    }


}
