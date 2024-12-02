package com.example.randify;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.randify.models.Song;

import java.util.HashMap;

public class PlayerService {
    private static PlayerService instance;
    private final Context context;
    private HashMap<String, Song> songMap = new HashMap<>();
    private Song currentSong;
    private MediaPlayer currentSongPlayer;

    private PlayerService(Context context) {
        this.context = context;
    }

    public static synchronized PlayerService getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerService(context);
        }

        return instance;
    }

    public void loadSongData() {
        songMap.put("Entry of Gladiators", new Song("Gladiator Guy", "Some album", "Entry of Gladiators", 15, R.raw.entry_of_gladiators));
        songMap.put("Fanfare", new Song("Fanfare Guy", "Some album", "Fanfare", 15, R.raw.fanfare));
        songMap.put("Ode to Joy", new Song("Beethoven", "Some album", "Ode to Joy", 15, R.raw.ode_to_joy));
        songMap.put("Overture", new Song("Overture Guy", "Some album", "Overture", 15, R.raw.overture));
    }

    public void playSong(String name) {
        if (songMap.containsKey(name) && context != null) {
            this.currentSong = songMap.get(name);

            if (this.currentSongPlayer != null) {
                if (this.currentSongPlayer.isPlaying()) {
                    this.currentSongPlayer.stop();
                }

                this.currentSongPlayer.release();
                this.currentSongPlayer = null;
            }
            this.currentSongPlayer = MediaPlayer.create(this.context, this.currentSong.getAudioResourceId());
            this.currentSongPlayer.start();
        }
    }
}
