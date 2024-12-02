package com.example.randify;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.TextView;

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
        songMap.put("Entry of Gladiators", new Song("Gladiator Guy", "Some album", "Entry of Gladiators", 15, R.raw.entry_of_gladiators));
        songMap.put("Fanfare", new Song("Fanfare Guy", "Some album", "Fanfare", 15, R.raw.fanfare));
        songMap.put("Ode to Joy", new Song("Beethoven", "Some album", "Ode to Joy", 15, R.raw.ode_to_joy));
        songMap.put("Overture", new Song("Overture Guy", "Some album", "Overture", 15, R.raw.overture));

        currentPlaylist = new SongLinkedList(R.raw.playlistpicture, "CSE214 Feels", "When you spend 10 hours on a homework just to realize you have no idea what you're doing.");

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
            this.currentSongPlayer.setVolume(2, 2);
            this.currentSongPlayer.start();

            updatePlayerBar(this.currentSong);
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

    public void setPlayerBarViews(TextView titleView, ImageButton playPauseButton) {
        this.songTitleTextView = titleView;
        this.playPauseButton = playPauseButton;
    }

    public void updatePlayerBar(Song newSong) {
        if (songTitleTextView != null) {
            songTitleTextView.setText(newSong.getName());
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

    private void cancelSong() {
        if (this.currentSongPlayer != null) {
            this.currentSongPlayer.stop();
            this.currentSongPlayer.release();
            this.currentSongPlayer = null;
        }
    }
}
