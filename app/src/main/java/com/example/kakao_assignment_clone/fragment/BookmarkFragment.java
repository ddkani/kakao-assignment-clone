package com.example.kakao_assignment_clone.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kakao_assignment_clone.databinding.FragmentBookmarkBinding;
import com.example.kakao_assignment_clone.dto.LikeItems;
import com.example.kakao_assignment_clone.dto.SearchImageDocument;
import com.example.kakao_assignment_clone.recycler.bookmark.BookmarkAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    FragmentBookmarkBinding binding;
    List<SearchImageDocument> dataset = new ArrayList<>();
    BookmarkAdapter adapter;

    public BookmarkFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false);

        adapter = new BookmarkAdapter(dataset, getContext());
        RecyclerView bookmarkView = binding.bookmarkResultView;
        bookmarkView.setAdapter(adapter);
        bookmarkView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loadBookmarks();

        return binding.getRoot();
    }

    private void loadBookmarks() {
        SharedPreferences preferences = requireContext().getSharedPreferences("file", Context.MODE_PRIVATE);
        String _likeItems = preferences.getString("likeItems", null);

        dataset.clear();

        if (_likeItems != null) {
            LikeItems likeItems = new Gson().fromJson(_likeItems, LikeItems.class);
            if (likeItems.items != null && !likeItems.items.isEmpty()) {
                dataset.addAll(likeItems.items);
            }
        }

        adapter.setBookmarks(dataset);

        if (dataset.isEmpty()) {
            binding.bookmarkEmptyText.setVisibility(View.VISIBLE);
            binding.bookmarkResultView.setVisibility(View.GONE);
        } else {
            binding.bookmarkEmptyText.setVisibility(View.GONE);
            binding.bookmarkResultView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
