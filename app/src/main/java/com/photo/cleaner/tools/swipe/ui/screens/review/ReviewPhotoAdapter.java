package com.photo.cleaner.tools.swipe.ui.screens.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.util.List;

public class ReviewPhotoAdapter extends RecyclerView.Adapter<ReviewPhotoAdapter.ViewHolder> {
    private List<PhotoItem> items;
    private OnPhotoClickListener listener;
    
    public interface OnPhotoClickListener {
        void onPhotoClick(PhotoItem photo);
    }
    
    public ReviewPhotoAdapter(List<PhotoItem> items, OnPhotoClickListener listener) {
        this.items = items;
        this.listener = listener;
    }
    
    public void updateData(List<PhotoItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_review_photo, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoItem photo = items.get(position);
        holder.bind(photo, listener);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
        
        public void bind(PhotoItem photo, OnPhotoClickListener listener) {
            Glide.with(itemView.getContext())
                .load(photo.getUri())
                .centerCrop()
                .into(imageView);
            
            itemView.setOnClickListener(v -> listener.onPhotoClick(photo));
        }
    }
}

