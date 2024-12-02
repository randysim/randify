package com.example.randify;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

        BottomNavigationView navView = findViewById(R.id.nav_view);

        TextView songTitleTextView = findViewById(R.id.songTitleTextView);
        ImageButton playPauseButton = findViewById(R.id.playPauseButton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);

        playerService.setPlayerBarViews(songTitleTextView, playPauseButton);


        playPauseButton.setOnClickListener(v -> playerService.togglePlayPause());
        nextButton.setOnClickListener(v -> playerService.playNext());
        prevButton.setOnClickListener(v -> playerService.playPrevious());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_library)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        playerService.updatePlayerBar(playerService.getCurrentSong());
    }

    private void showErrorSnackbar(String errorMessage) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, "Error: " + errorMessage, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", v -> {})
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }


}