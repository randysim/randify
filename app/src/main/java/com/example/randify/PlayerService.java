package com.example.randify;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.randify.adapters.PlaylistAdapter;
import com.example.randify.models.Song;
import com.example.randify.models.SongLinkedList;

import java.io.IOException;
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
    private TextView playlistLengthView;
    private ProgressBar songProgress;
    private boolean first = true;
    private ImageButton playlistPlayPause;
    private ImageButton shuffleButton;
    private boolean shuffle = false;
    private Handler progressHandler;
    private Runnable progressRunnable;

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
        songMap.put("LIMBO", new Song("keshi", "Some album", "LIMBO", R.raw.limbo, R.drawable.keshi1));
        songMap.put("dying to see you", new Song("bixby", "Some album", "dying to see you", R.raw.dyingtoseeyou, R.drawable.bixby1));

        currentPlaylist = new SongLinkedList(R.drawable.playlistpicture, "cse214 feels", "for when hw7 beats you down");

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
            this.songProgress.setProgress(0);
            this.currentSongPlayer = MediaPlayer.create(this.context, this.currentSong.getAudioResourceId());

            if (progressHandler != null && progressRunnable != null) {
                progressHandler.removeCallbacks(progressRunnable);
            }

            currentSongPlayer.start();
            this.progressHandler = new Handler();
            this.progressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (currentSongPlayer != null) {
                        int currentProgress = (int) ((currentSongPlayer.getCurrentPosition()/10)/currentSong.getLength());
                        songProgress.setProgress(currentProgress);
                        progressHandler.postDelayed(this, 100);
                    }
                }
            };
            progressHandler.postDelayed(progressRunnable, 0);

            currentSongPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    progressHandler.removeCallbacks(progressRunnable);
                    progressHandler = null;
                    progressRunnable = null;

                    playNext();
                }
            });

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

    public void setPlayerBarViews(
            TextView titleView,
            ImageButton playPauseButton,
            TextView artistNameView,
            ImageView albumArtView,
            CardView playerView,
            TextView playlistLengthView,
            ProgressBar songProgress,
            ImageButton playlistPlayPause,
            ImageButton playlistShuffle
    ) {
        this.songTitleTextView = titleView;
        this.playPauseButton = playPauseButton;
        this.songArtistView = artistNameView;
        this.albumArtView = albumArtView;
        this.playerView = playerView;
        this.playlistLengthView = playlistLengthView;
        this.songProgress = songProgress;
        this.playlistPlayPause = playlistPlayPause;
        this.shuffleButton = playlistShuffle;
    }

    public void updatePlaylist() {
        playlistLengthView.setText(
            formatSpotifyTime(currentPlaylist.getTotalTime())
        );
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
                if (first) {
                    first = false;
                    this.playSong(this.currentSong.getName());
                } else {
                    currentSongPlayer.start();
                }
            }
            updatePlayPauseButton();
        }
    }

    public void toggleShuffle() {
        shuffle = !shuffle;
        updateShuffleButton();
    }

    public void playNext() {
        if (shuffle) {
            playRandom();
            return;
        }
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
            playlistPlayPause.setImageResource(currentSongPlayer.isPlaying() ?
                    R.drawable.pause_circle : R.drawable.play_circle);
        }
    }

    private void updateShuffleButton() {
        if (shuffleButton != null) {
            if (shuffle) {
                shuffleButton.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                shuffleButton.setColorFilter(ContextCompat.getColor(context, R.color.textSecondary));
            }
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
        hsv[2] = Math.max(Math.min(0.5f, hsv[2] * 0.5f), 0.2f);
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

    private String formatSpotifyTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, remainingSeconds);
        } else {
            return String.format("0m %ds", remainingSeconds);
        }
    }


    public int getSongDurationSeconds(int audioResourceId) {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(audioResourceId);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            retriever.close();
            afd.close();
        } catch (IOException e) {
            return -1;
        }

        long durationInMilliseconds = Long.parseLong(duration);
        int durationInSeconds = (int) Math.ceil(durationInMilliseconds / 1000);
        return durationInSeconds;
    }
}
