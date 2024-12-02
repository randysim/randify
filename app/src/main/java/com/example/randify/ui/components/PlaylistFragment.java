package com.example.randify.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.randify.PlayerService;
import com.example.randify.R;
import com.example.randify.adapters.PlaylistAdapter;

public class PlaylistFragment extends Fragment {
    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private PlayerService playerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playerService = PlayerService.getInstance(getContext());
        playlistAdapter = new PlaylistAdapter(playerService.getCurrentPlaylist(), playerService);
        playlistRecyclerView.setAdapter(playlistAdapter);

        return view;
    }

    public void updatePlaylist() {
        if (playlistAdapter != null) {
            playlistAdapter.notifyDataSetChanged();
        }
    }
}

