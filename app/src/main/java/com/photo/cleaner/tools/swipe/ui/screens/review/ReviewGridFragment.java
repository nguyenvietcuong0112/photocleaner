package com.photo.cleaner.tools.swipe.ui.screens.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;

import java.util.ArrayList;
import java.util.List;

public class ReviewGridFragment extends Fragment {
    private static final String ARG_TAB_POSITION = "tab_position";
    
    private RecyclerView recyclerView;
    private ReviewPhotoAdapter adapter;
    private ReviewViewModel viewModel;
    private int tabPosition;
    
    public static ReviewGridFragment newInstance(int tabPosition, ReviewViewModel viewModel) {
        ReviewGridFragment fragment = new ReviewGridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        fragment.viewModel = viewModel;
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_grid, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tabPosition = getArguments() != null ? getArguments().getInt(ARG_TAB_POSITION, 0) : 0;
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        
        adapter = new ReviewPhotoAdapter(new ArrayList<>(), photo -> {
            // Change decision
            if (tabPosition == 0) {
                // Keep tab - move to delete
                viewModel.changeDecision(photo.getId(), PhotoDecision.DELETE);
            } else {
                // Delete tab - move to keep
                viewModel.changeDecision(photo.getId(), PhotoDecision.KEEP);
            }
        });
        
        recyclerView.setAdapter(adapter);
        
        viewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            if (uiState != null) {
                List<PhotoItem> photos = tabPosition == 0 ? 
                    uiState.getKeepPhotos() : uiState.getDeletePhotos();
                adapter.updateData(photos);
            }
        });
    }
}

