package com.photo.cleaner.tools.swipe.ui.screens.intro;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class IntroPagerAdapter extends FragmentStateAdapter {
    
    public IntroPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return IntroPageFragment.newInstance(position);
    }
    
    @Override
    public int getItemCount() {
        return 3;
    }
}

