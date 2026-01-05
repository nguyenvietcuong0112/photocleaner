package com.photo.cleaner.tools.swipe.ui.screens.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.photo.cleaner.tools.swipe.MainActivity;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.ui.screens.permission.PermissionFragment;

public class IntroFragment extends Fragment {
    private ViewPager2 viewPager;
    private IntroPagerAdapter adapter;
    private Button btnNext;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewPager = view.findViewById(R.id.viewPager);
        btnNext = view.findViewById(R.id.btnNext);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        
        adapter = new IntroPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);
        
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
        
        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                navigateToPermission();
            }
        });
        
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getItemCount() - 1) {
                    btnNext.setText(R.string.intro_button_start);
                } else {
                    btnNext.setText(getString(R.string.next));
                }
            }
        });
    }
    
    private void navigateToPermission() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setIntroShown(true);
        }
        
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PermissionFragment());
        transaction.commit();
    }
}

