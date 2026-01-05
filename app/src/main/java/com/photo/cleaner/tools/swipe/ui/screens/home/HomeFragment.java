package com.photo.cleaner.tools.swipe.ui.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.MonthProgress;
import com.photo.cleaner.tools.swipe.data.OnThisDayData;
import com.photo.cleaner.tools.swipe.ui.screens.swipe.SwipeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;
    private MonthProgressAdapter adapter;
    private View cardOnThisDay;
    private TextView textOnThisDay;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        cardOnThisDay = view.findViewById(R.id.cardOnThisDay);
        textOnThisDay = view.findViewById(R.id.textOnThisDay);
        
        adapter = new MonthProgressAdapter(new ArrayList<>(), this::onMonthClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        
        viewModel = new ViewModelProvider(this, new HomeViewModelFactory(requireContext())).get(HomeViewModel.class);
        
        viewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            if (uiState != null) {
                updateUI(uiState);
            }
        });
        
        cardOnThisDay.setOnClickListener(v -> navigateToSwipe("on_this_day", ""));
        
        viewModel.loadData();
    }
    
    private void updateUI(HomeUiState uiState) {
        // Update On This Day card
        OnThisDayData onThisDay = uiState.getOnThisDay();
        if (onThisDay != null) {
            cardOnThisDay.setVisibility(View.VISIBLE);
            textOnThisDay.setText(String.format(getString(R.string.home_on_this_day_subtitle), 
                onThisDay.getYearsAgo(), onThisDay.getPhotoCount()));
        } else {
            cardOnThisDay.setVisibility(View.GONE);
        }
        
        // Update monthly list
        adapter.updateData(uiState.getMonthlyProgress());
    }
    
    private void onMonthClick(MonthProgress progress) {
        navigateToSwipe("month", progress.getYearMonth().toString());
    }
    
    private void navigateToSwipe(String sourceType, String sourceId) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        SwipeFragment swipeFragment = SwipeFragment.newInstance(sourceType, sourceId);
        transaction.replace(R.id.fragment_container, swipeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

