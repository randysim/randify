package com.example.randify.ui.activities.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.randify.R;
import com.example.randify.databinding.FragmentHomeBinding;
import com.example.randify.ui.components.PlaylistFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showPlaylistFragment();
        return root;
    }

    private void showPlaylistFragment() {
        PlaylistFragment playlistFragment = new PlaylistFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.playlistContainer, playlistFragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}