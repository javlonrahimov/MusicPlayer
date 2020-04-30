package com.rahimovjavlon1212.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.musicplayer.R;
import com.rahimovjavlon1212.musicplayer.models.MusicData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectableAdapter extends RecyclerView.Adapter<SelectableMusicViewHolder> {
    private List<MusicData> mData;
    private LayoutInflater mInflater;
    public OnItemClicked onItemClicked;

    public SelectableAdapter(Context context, List<MusicData> mData) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<MusicData> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectableMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_music_selectable, parent, false);
        return new SelectableMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectableMusicViewHolder holder, final int position) {
        holder.musicTitle.setText(mData.get(position).getTitle());
        holder.musicArtist.setText(mData.get(position).getArtist());
        if (mData.get(position).isSelected()){
            holder.selectIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }else {
            holder.selectIcon.setImageResource(R.drawable.ic_panorama_fish_eye_black_24dp);
        }
        holder.musicImage.setImageBitmap(mData.get(position).getBitmap());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClicked != null) {
                if (!mData.get(position).isSelected()){
                    holder.selectIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    mData.get(position).setSelected(true);
                }else {
                    holder.selectIcon.setImageResource(R.drawable.ic_panorama_fish_eye_black_24dp);
                    mData.get(position).setSelected(false);
                }
                onItemClicked.onClick(mData.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClicked {
        void onClick(MusicData musicData, int position);
    }
}

class SelectableMusicViewHolder extends RecyclerView.ViewHolder {

    TextView musicTitle;
    TextView musicArtist;
    CircleImageView musicImage;
    ImageView selectIcon;

    SelectableMusicViewHolder(@NonNull View itemView) {
        super(itemView);
        selectIcon = itemView.findViewById(R.id.selectIconItemMusicSelectable);
        musicArtist = itemView.findViewById(R.id.artistItemMusicSelectable);
        musicImage = itemView.findViewById(R.id.imageItemMusicSelectable);
        musicTitle = itemView.findViewById(R.id.titleItemMusicSelectable);
    }


}
