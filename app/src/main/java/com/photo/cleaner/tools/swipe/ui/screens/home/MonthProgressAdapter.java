package com.photo.cleaner.tools.swipe.ui.screens.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.MonthProgress;
import com.photo.cleaner.tools.swipe.data.MonthStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonthProgressAdapter extends RecyclerView.Adapter<MonthProgressAdapter.ViewHolder> {
    private List<MonthProgress> items;
    private OnMonthClickListener listener;
    
    public interface OnMonthClickListener {
        void onMonthClick(MonthProgress progress);
    }
    
    public MonthProgressAdapter(List<MonthProgress> items, OnMonthClickListener listener) {
        this.items = items;
        this.listener = listener;
    }
    
    public void updateData(List<MonthProgress> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_month_progress, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthProgress progress = items.get(position);
        holder.bind(progress, listener);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textMonth;
        private TextView textStatus;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMonth = itemView.findViewById(R.id.textMonth);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
        
        public void bind(MonthProgress progress, OnMonthClickListener listener) {
            textMonth.setText(progress.getYearMonth().format(
                DateTimeFormatter.ofPattern("MMMM yyyy")
            ));
            
            String statusText;
            switch (progress.getStatus()) {
                case NOT_STARTED:
                    statusText = itemView.getContext().getString(R.string.home_not_started);
                    break;
                case IN_PROGRESS:
                    statusText = itemView.getContext().getString(R.string.home_in_progress) 
                        + " (" + progress.getReviewedPhotos() + "/" + progress.getTotalPhotos() + ")";
                    break;
                case COMPLETED:
                    statusText = itemView.getContext().getString(R.string.home_completed);
                    break;
                default:
                    statusText = "";
            }
            textStatus.setText(statusText);
            
            itemView.setOnClickListener(v -> listener.onMonthClick(progress));
        }
    }
}

