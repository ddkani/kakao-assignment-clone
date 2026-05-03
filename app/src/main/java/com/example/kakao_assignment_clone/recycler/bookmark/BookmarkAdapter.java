package com.example.kakao_assignment_clone.recycler.bookmark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kakao_assignment_clone.databinding.ItemBookmarkBinding;
import com.example.kakao_assignment_clone.dto.LikeItems;
import com.example.kakao_assignment_clone.dto.SearchImageDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<ItemBookmarkViewHolder> {

    private List<SearchImageDocument> dataset;
    private Context context;

    public BookmarkAdapter(List<SearchImageDocument> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBookmarks(List<SearchImageDocument> newDataset) {
        dataset.clear();
        dataset.addAll(newDataset);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemBookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookmarkBinding binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ItemBookmarkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBookmarkViewHolder holder, int position) {
        SearchImageDocument document = dataset.get(position);
        ItemBookmarkBinding binding = holder.binding;

        Glide.with(context).load(document.getThumbnailUrl()).into(binding.bookmarkImage);

        binding.bookmarkHeart.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            // SharedPreferences에서 해당 아이템 제거
            SharedPreferences preferences = context.getSharedPreferences("file", Context.MODE_PRIVATE);
            String _likeItems = preferences.getString("likeItems", null);

            if (_likeItems != null) {
                LikeItems likeItems = new Gson().fromJson(_likeItems, LikeItems.class);
                for (int i = 0; i < likeItems.items.size(); i++) {
                    if (likeItems.items.get(i).getImageUrl().equals(document.getImageUrl())) {
                        likeItems.items.remove(i);
                        break;
                    }
                }
                String _newLikeItems = new GsonBuilder().create().toJson(likeItems, LikeItems.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("likeItems", _newLikeItems);
                editor.apply();
            }

            // 리스트에서 제거 + 애니메이션
            dataset.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
