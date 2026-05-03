package com.example.kakao_assignment_clone.recycler.search_iamge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kakao_assignment_clone.R;
import com.example.kakao_assignment_clone.databinding.ItemSearchResultBinding;
import com.example.kakao_assignment_clone.dto.LikeItems;
import com.example.kakao_assignment_clone.dto.SearchImageDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<ItemSearchResultViewHolder> {

    private List<SearchImageDocument> dataset; // Adapter 에서 가지고 있는 데이터셋
    private Context context; // Glide 사용 시 필요

    public SearchResultAdapter(List<SearchImageDocument> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSearchResults(List<SearchImageDocument> newDataset) {
        dataset.clear();
        dataset.addAll(newDataset);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 새로운 SearchImageViewHolder 를 생성하여 반환한다.
        // ** 데이터의 렌더링은 수행하지 않는다.

        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ItemSearchResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchResultViewHolder holder, int position) {
        SearchImageDocument document = dataset.get(position);
        ItemSearchResultBinding binding = holder.binding;

        Glide.with(context).load(document.getThumbnailUrl()).into(binding.itemSearchPreviewImage);
        Glide.with(context).load(document.getImageUrl()).into(binding.itemSearchImage);

        binding.itemSearchDescription.setText(String.format(
            "%s\n%s",
            document.getDisplaySiteName(),
            document.getDatetime()
        ));

        // 좋아요 상태 초기화
        boolean isLiked = isItemLiked(document.getImageUrl());
        binding.itemSearchLike.setBackgroundResource(isLiked ? R.drawable.like_filled : R.drawable.like);

        binding.itemSearchLike.setOnClickListener(view -> {
            SharedPreferences preferences = context.getSharedPreferences("file", Context.MODE_PRIVATE);
            String _likeItems = preferences.getString("likeItems", null);
            LikeItems likeItems;

            if (_likeItems == null) {
                likeItems = new LikeItems();
            } else {
                likeItems = new Gson().fromJson(_likeItems, LikeItems.class);
            }

            // imageUrl 기준 토글
            boolean alreadyLiked = false;
            for (int i = 0; i < likeItems.items.size(); i++) {
                if (likeItems.items.get(i).getImageUrl().equals(document.getImageUrl())) {
                    likeItems.items.remove(i);
                    alreadyLiked = true;
                    break;
                }
            }

            if (!alreadyLiked) {
                likeItems.items.add(document);
            }

            String _newLikeItems = new GsonBuilder().create().toJson(likeItems, LikeItems.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("likeItems", _newLikeItems);
            editor.apply();

            // 아이콘 즉시 전환
            binding.itemSearchLike.setBackgroundResource(alreadyLiked ? R.drawable.like : R.drawable.like_filled);
        });
    }

    private boolean isItemLiked(String imageUrl) {
        SharedPreferences preferences = context.getSharedPreferences("file", Context.MODE_PRIVATE);
        String _likeItems = preferences.getString("likeItems", null);
        if (_likeItems == null) return false;

        LikeItems likeItems = new Gson().fromJson(_likeItems, LikeItems.class);
        for (SearchImageDocument item : likeItems.items) {
            if (item.getImageUrl().equals(imageUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
