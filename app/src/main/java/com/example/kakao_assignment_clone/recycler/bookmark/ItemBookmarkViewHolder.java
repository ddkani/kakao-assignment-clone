package com.example.kakao_assignment_clone.recycler.bookmark;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kakao_assignment_clone.databinding.ItemBookmarkBinding;

public class ItemBookmarkViewHolder extends RecyclerView.ViewHolder {
    public final ItemBookmarkBinding binding;

    public ItemBookmarkViewHolder(ItemBookmarkBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
