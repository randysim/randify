package com.example.randify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.randify.adapters.PlaylistAdapter;
import com.example.randify.models.Song;
import com.example.randify.models.SongLinkedList;

import java.util.HashMap;

public class PlayerService {
    private static PlayerService instance;
    private final Context context;
    private HashMap<String, Song> songMap = new HashMap<>();
    private HashMap<String, SongLinkedList> playlistMap = new HashMap<>();
    private Song currentSong;
    private MediaPlayer currentSongPlayer;
    private SongLinkedList currentPlaylist;
    private TextView songTitleTextView;
    private ImageButton playPauseButton;
    private PlaylistAdapter adapter;
    private TextView songArtistView;
    private ImageView albumArtView;
    private CardView playerView;

    private PlayerService(Context context) {
        this.context = context;
    }

    public static synchronized PlayerService getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerService(context);
        }

        return instance;
    }

    public void loadInitialData() {
        songMap.put("Entry of Gladiators", new Song("Gladiator Guy", "Some album", "Entry of Gladiators", 15, R.raw.entry_of_gladiators, R.drawable.defaultimage));
        songMap.put("Fanfare", new Song("Fanfare Guy", "Some album", "Fanfare", 15, R.raw.fanfare, R.drawable.defaultimage));
        songMap.put("Ode to Joy", new Song("Beethoven", "Some album", "Ode to Joy", 15, R.raw.ode_to_joy, R.drawable.defaultimage));
        songMap.put("Overture", new Song("Overture Guy", "Some album", "Overture", 15, R.raw.overture, R.drawable.defaultimage));
        songMap.put("Overture1", new Song("Overture Guy", "Some album", "Overture1", 15, R.raw.overture, R.drawable.defaultimage));
        songMap.put("Overture2", new Song("Overture Guy", "Some album", "Overture2", 15, R.raw.overture, R.drawable.defaultimage));

        currentPlaylist = new SongLinkedList(R.drawable.playlistpicture, "CSE214 Feels", "for when hw7 beats you down");

        for (Song song : songMap.values()) {
            currentPlaylist.addSongToEnd(song);
        }

        playlistMap.put("CSE214 Feels", currentPlaylist);

        currentPlaylist.setCursorToBeginning();
        this.currentSong = currentPlaylist.getCurrentSong();
        this.currentSongPlayer = MediaPlayer.create(this.context, this.currentSong.getAudioResourceId());
    }


    public void playSong(String name) {
        if (songMap.containsKey(name) && context != null) {
            this.currentSong = songMap.get(name);
            cancelSong();

            this.currentSongPlayer = MediaPlayer.create(this.context, this.currentSong.getAudioResourceId());

            currentSongPlayer.start();
            notifyAdapter();
            updatePlayerBar(currentSong);
        }
    }

    public void play(String name) {
        this.currentPlaylist.play(name);
    }

    public void playRandom() {
        this.currentPlaylist.random();
    }

    public void addSong(String name) {
        if (songMap.containsKey(name)) {
            this.currentPlaylist.addSongToEnd(songMap.get(name));
        }
    }

    public void removeSong(String name) {
        this.currentPlaylist.removeSong(name);
    }

    public void setPlayerBarViews(TextView titleView, ImageButton playPauseButton, TextView artistNameView, ImageView albumArtView, CardView playerView) {
        this.songTitleTextView = titleView;
        this.playPauseButton = playPauseButton;
        this.songArtistView = artistNameView;
        this.albumArtView = albumArtView;
        this.playerView = playerView;
    }

    public void updatePlayerBar(Song newSong) {
        if (songTitleTextView != null) {
            songTitleTextView.setText(newSong.getName());
        }
        if (songArtistView != null) {
            songArtistView.setText(newSong.getArtist());
        }
        if (albumArtView != null) {
            albumArtView.setImageResource(newSong.getImageResourceId());
        }
        if (playerView != null) {
            playerView.setBackgroundColor(getAverageColor(newSong.getImageResourceId()));
        }
        updatePlayPauseButton();
    }

    public void togglePlayPause() {
        if (currentSongPlayer != null) {
            if (currentSongPlayer.isPlaying()) {
                currentSongPlayer.pause();
            } else {
                currentSongPlayer.start();
            }
            updatePlayPauseButton();
        }
    }

    public void playNext() {
        currentPlaylist.cursorForward();
        playSong(currentPlaylist.getCurrentSong().getName());
    }

    public void playPrevious() {
        currentPlaylist.cursorBackward();
        playSong(currentPlaylist.getCurrentSong().getName());
    }

    private void updatePlayPauseButton() {
        if (playPauseButton != null) {
            playPauseButton.setImageResource(currentSongPlayer.isPlaying() ?
                    android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        }
    }

    public SongLinkedList getCurrentPlaylist() {
        return this.currentPlaylist;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setAdapater(PlaylistAdapter adapter) {
        this.adapter = adapter;
    }

    public void notifyAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    private void cancelSong() {
        if (this.currentSongPlayer != null) {
            this.currentSongPlayer.stop();
            this.currentSongPlayer.release();
            this.currentSongPlayer = null;
        }
    }

    private int calculateAverageColor(Bitmap bitmap) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i += 5) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return Color.rgb(R / n, G / n, B / n);
    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.25f;
        return Color.HSVToColor(hsv);
    }

    private int getAverageColor(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int avgColor = darkenColor(calculateAverageColor(bitmap));

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return avgColor;
    }
}
