package com.example.randify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.randify.PlayerService;
import com.example.randify.R;
import com.example.randify.models.Song;
import com.example.randify.models.SongLinkedList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {
    private SongLinkedList playlist;
    private PlayerService playerService;

    public PlaylistAdapter(SongLinkedList playlist, PlayerService playerService) {
        this.playlist = playlist;
        this.playerService = playerService;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_card, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = getSongAtPosition(position);
        holder.songName.setText(song.getName());
        holder.artistName.setText(song.getArtist());

        // Check if this song is currently playing
        if (playerService.getCurrentSong() != null &&
                playerService.getCurrentSong().getName().equals(song.getName())) {
            holder.songName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
        } else {
            holder.songName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textPrimary));
        }

        holder.artistName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textSecondary));
        holder.itemView.setOnClickListener(v -> {
            if (playerService.getCurrentSong().getName().equals(song.getName())) {
                playerService.togglePlayPause();
            } else {
                playerService.play(song.getName());
            }
        });

        holder.songImage.setImageResource(song.getImageResourceId());
    }


    @Override
    public int getItemCount() {
        return playlist.getSize();
    }

    private Song getSongAtPosition(int position) {
        // Implement this method to return the song at the given position in the playlist
        // You might need to add a method in SongLinkedList to get a song by index
        SongLinkedList currentPlaylist = PlayerService.getInstance(null).getCurrentPlaylist();
        if (currentPlaylist == null) return null;

        return currentPlaylist.getSongAtIndex(position);
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songName;
        TextView artistName;

        SongViewHolder(View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.songImage);
            songName = itemView.findViewById(R.id.songName);
            artistName = itemView.findViewById(R.id.artistName);
        }
    }

}
