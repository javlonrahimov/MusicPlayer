package com.rahimovjavlon1212.musicplayer.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rahimovjavlon1212.musicplayer.R;

public class CreatePlaylistDialog extends AlertDialog {

    private EditText playlistName;

    public OnResultListener onResultListener;

    private Button createButton;

    public CreatePlaylistDialog(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.create_playlist_dialog, null, false);

        playlistName = view.findViewById(R.id.playlistNameCreateDialog);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        createButton = view.findViewById(R.id.createButton);

        createButton.setOnClickListener(e -> {
            if (!playlistName.getText().toString().isEmpty()) {
                if (onResultListener != null) {
                    onResultListener.onClick(playlistName.getText().toString());
                }
            } else {
                Toast.makeText(context, "Please enter the playlist name!!!", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(e -> cancel());

        setView(view);
        show();
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName.setText(playlistName);
    }

    public void setButtonText(String string) {
        createButton.setText(string);
    }

    public interface OnResultListener {
        void onClick(String namePlaylist);
    }
}
