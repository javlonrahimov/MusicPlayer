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

    public void setData(List<MusicData> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicViewHolder holder, final int position) {
        holder.musicTitle.setText(mData.get(position).getTitle());
        holder.musicArtist.setText(mData.get(position).getArtist());
        if (mData.get(position).getBitmap() != null)
            holder.musicImage.setImageBitmap(mData.get(position).getBitmap());
        else
            holder.musicImage.setImageResource(R.drawable.image);
        if (mData.get(position).isFocused()) {
            holder.musicImage.setBorderWidth(5);
            holder.musicImage.setBorderColor(context.getResources().getColor(R.color.colorPrimary));
            holder.musicArtist.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
            holder.musicTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.musicImage.setBorderWidth(0);
            holder.musicArtist.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.musicTitle.setTextColor(context.getResources().getColor(android.R.color.black));
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClicked != null) {
                onItemClicked.onClick(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClicked {
        void onClick(MusicData musicData);
    }

    public void setFocus(String id) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMusicId().equals(id)) {
                mData.get(i).setFocused(true);
            }
            if (!mData.get(i).getMusicId().equals(id) && mData.get(i).isFocused()) {
                mData.get(i).setFocused(false);
            }
        }
        notifyDataSetChanged();
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
