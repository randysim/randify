package com.example.randify;

import android.content.Context;
import android.media.MediaPlayer;

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

        currentPlaylist = new SongLinkedList();
        playlistMap.put("Default Playlist", currentPlaylist);
    }

    public void playSong(String name) {
        if (songMap.containsKey(name) && context != null) {
            this.currentSong = songMap.get(name);
            cancelSong();

            this.currentSongPlayer = MediaPlayer.create(this.context, this.currentSong.getAudioResourceId());
            this.currentSongPlayer.start();
        }
    }

    public void play(String name) {
        this.currentPlaylist.play(name);
    }

    public void pause() {
        if (this.currentSongPlayer.isPlaying()) {
            currentSongPlayer.pause();
        }
    }

    public void next() {
        cancelSong();

        this.currentPlaylist.cursorForward();
        this.currentPlaylist.play(this.currentPlaylist.getCurrentSong().getName());
    }

    public void prev() {
        cancelSong();
        this.currentPlaylist.cursorBackward();
        this.currentPlaylist.play(this.currentPlaylist.getCurrentSong().getName());
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

    private void cancelSong() {
        if (this.currentSongPlayer != null) {
            this.currentSongPlayer.stop();
            this.currentSongPlayer.release();
            this.currentSongPlayer = null;
        }
    }
}
