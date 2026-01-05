package com.photo.cleaner.tools.swipe.ui.screens.review;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.util.List;

public class ReviewPagerAdapter extends FragmentStateAdapter {
    private ReviewViewModel viewModel;
    
    public ReviewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ReviewViewModel viewModel) {
        super(fragmentActivity);
        this.viewModel = viewModel;
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ReviewGridFragment.newInstance(position, viewModel);
    }
    
    @Override
    public int getItemCount() {
        return 2; // Keep and Delete tabs
    }
}

