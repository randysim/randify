package com.example.randify;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.randify.adapters.PlaylistAdapter;
import com.example.randify.ui.WrapContentLinearLayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.randify.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Create the PlayerService singleton */
        PlayerService playerService = PlayerService.getInstance(getApplicationContext());
        playerService.loadInitialData();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setElevation(0);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        TextView songTitleTextView = findViewById(R.id.songTitleTextView);
        ImageButton playPauseButton = findViewById(R.id.playPauseButton);
        ImageButton playlistPlayPause = findViewById(R.id.playButton);
        ImageButton shuffleButton = findViewById(R.id.shuffleButton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        TextView songArtistTextView = findViewById(R.id.artistTextView);
        ImageView albumArtView = findViewById(R.id.albumArtImageView);
        CardView cardView = findViewById(R.id.playerCard);
        TextView playlistLengthView = findViewById(R.id.playlistLength);
        ProgressBar songProgress = findViewById(R.id.songProgressBar);

        playerService.setPlayerBarViews(
                songTitleTextView,
                playPauseButton,
                songArtistTextView,
                albumArtView,
                cardView,
                playlistLengthView,
                songProgress,
                playlistPlayPause,
                shuffleButton
        );
        ImageView imageView = findViewById(R.id.playlistImageView);
        imageView.setImageResource(R.drawable.playlistpicture);

        shuffleButton.setOnClickListener(v -> playerService.toggleShuffle());
        playlistPlayPause.setOnClickListener(v -> playerService.togglePlayPause());
        playPauseButton.setOnClickListener(v -> playerService.togglePlayPause());
        nextButton.setOnClickListener(v -> playerService.playNext());
        prevButton.setOnClickListener(v -> playerService.playPrevious());

        RecyclerView songListRecyclerView = findViewById(R.id.playlistRecyclerView);
        songListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlaylistAdapter playlistAdapter= new PlaylistAdapter(playerService.getCurrentPlaylist(), playerService);
        songListRecyclerView.setAdapter(playlistAdapter);
        songListRecyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        songListRecyclerView.requestLayout();
        songListRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getApplicationContext()));
        playerService.setAdapater(playlistAdapter);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        playerService.updatePlayerBar(playerService.getCurrentSong());
        playerService.updatePlaylist();
    }

    private void showErrorSnackbar(String errorMessage) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "Error: " + errorMessage, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", v -> {})
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }


}