package com.photo.cleaner.tools.swipe.ui.screens.review;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;
import com.photo.cleaner.tools.swipe.ui.screens.home.HomeFragment;

public class ReviewFragment extends Fragment {
    private static final String ARG_SOURCE_TYPE = "source_type";
    private static final String ARG_SOURCE_ID = "source_id";
    
    private ReviewViewModel viewModel;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TextView textSummary;
    private TextView textStorage;
    private Button btnFinish;
    private ReviewPagerAdapter pagerAdapter;
    
    public static ReviewFragment newInstance(String sourceType, String sourceId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SOURCE_TYPE, sourceType);
        args.putString(ARG_SOURCE_ID, sourceId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Bundle args = getArguments();
        if (args == null) return;
        
        String sourceType = args.getString(ARG_SOURCE_TYPE);
        String sourceId = args.getString(ARG_SOURCE_ID);
        
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        textSummary = view.findViewById(R.id.textSummary);
        textStorage = view.findViewById(R.id.textStorage);
        btnFinish = view.findViewById(R.id.btnFinish);
        
        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        viewModel = new ViewModelProvider(this,
            new ReviewViewModelFactory(sourceType, sourceId, requireContext()))
            .get(ReviewViewModel.class);
        
        pagerAdapter = new ReviewPagerAdapter(requireActivity(), viewModel);
        viewPager.setAdapter(pagerAdapter);
        
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Keep");
            } else {
                tab.setText("Delete");
            }
        }).attach();
        
        viewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            if (uiState != null) {
                updateUI(uiState);
                pagerAdapter.notifyDataSetChanged();
            }
        });
        
        btnFinish.setOnClickListener(v -> showConfirmDialog());
        
        viewModel.loadReviewData();
    }
    
    private void updateUI(ReviewUiState state) {
        textSummary.setText("Reviewed: " + state.getTotalReviewed() + 
            " | To delete: " + state.getDeletePhotos().size());
        textStorage.setText("Storage saved: " + 
            ReviewViewModel.formatFileSize(state.getStorageSaved()));
        
        btnFinish.setEnabled(state.getDeletePhotos() != null && !state.getDeletePhotos().isEmpty());
        
        // Update tab labels
        TabLayout.Tab keepTab = tabLayout.getTabAt(0);
        TabLayout.Tab deleteTab = tabLayout.getTabAt(1);
        if (keepTab != null) {
            keepTab.setText("Keep (" + state.getKeepPhotos().size() + ")");
        }
        if (deleteTab != null) {
            deleteTab.setText("Delete (" + state.getDeletePhotos().size() + ")");
        }
    }
    
    private void showConfirmDialog() {
        ReviewUiState state = viewModel.getUiState().getValue();
        if (state == null) return;
        
        new AlertDialog.Builder(requireContext())
            .setTitle(R.string.review_confirm_title)
            .setMessage(String.format(getString(R.string.review_confirm_message), state.getDeletePhotos().size()))
            .setPositiveButton(R.string.review_confirm_yes, (dialog, which) -> {
                viewModel.finishAndDelete(this::navigateToHome);
            })
            .setNegativeButton(R.string.review_confirm_no, null)
            .show();
    }
    
    private void navigateToHome() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment());
        // Clear back stack
        for (int i = 0; i < getParentFragmentManager().getBackStackEntryCount(); i++) {
            getParentFragmentManager().popBackStack();
        }
        transaction.commit();
    }
}

