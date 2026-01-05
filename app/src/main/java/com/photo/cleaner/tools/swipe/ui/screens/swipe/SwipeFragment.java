package com.photo.cleaner.tools.swipe.ui.screens.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.data.PhotoDecision;
import com.photo.cleaner.tools.swipe.data.PhotoItem;
import com.photo.cleaner.tools.swipe.ui.screens.review.ReviewFragment;

public class SwipeFragment extends Fragment {
    private static final String ARG_SOURCE_TYPE = "source_type";
    private static final String ARG_SOURCE_ID = "source_id";
    
    private SwipeViewModel viewModel;
    private ImageView imageView;
    private TextView textProgress;
    private View indicatorKeep;
    private View indicatorDelete;
    
    private float startX;
    private boolean isDragging = false;
    
    public static SwipeFragment newInstance(String sourceType, String sourceId) {
        SwipeFragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SOURCE_TYPE, sourceType);
        args.putString(ARG_SOURCE_ID, sourceId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Bundle args = getArguments();
        if (args == null) return;
        
        String sourceType = args.getString(ARG_SOURCE_TYPE);
        String sourceId = args.getString(ARG_SOURCE_ID);
        
        imageView = view.findViewById(R.id.imageView);
        textProgress = view.findViewById(R.id.textProgress);
        indicatorKeep = view.findViewById(R.id.indicatorKeep);
        indicatorDelete = view.findViewById(R.id.indicatorDelete);
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }
        
        viewModel = new ViewModelProvider(this, 
            new SwipeViewModelFactory(sourceType, sourceId, requireContext()))
            .get(SwipeViewModel.class);
        
        viewModel.getUiState().observe(getViewLifecycleOwner(), uiState -> {
            if (uiState != null) {
                updateUI(uiState);
            }
        });
        
        setupTouchHandling();
        viewModel.loadPhotos();
    }
    
    private void setupTouchHandling() {
        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    isDragging = false;
                    return true;
                    
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - startX;
                    if (Math.abs(deltaX) > 20) {
                        isDragging = true;
                        updateDragIndicators(deltaX);
                        imageView.setTranslationX(deltaX);
                        imageView.setAlpha(0.9f);
                        imageView.setScaleX(0.95f);
                        imageView.setScaleY(0.95f);
                    }
                    return true;
                    
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!isDragging) {
                                // Single tap - undo
                        viewModel.undoLastDecision();
                    } else {
                        float finalDeltaX = event.getX() - startX;
                        float threshold = v.getWidth() * 0.3f;
                        
                        if (finalDeltaX > threshold) {
                            // Swipe right - KEEP
                            SwipeUiState state = viewModel.getUiState().getValue();
                            if (state != null && state.getCurrentIndex() < state.getPhotos().size()) {
                                PhotoItem photo = state.getPhotos().get(state.getCurrentIndex());
                                viewModel.decidePhoto(photo.getId(), PhotoDecision.KEEP);
                                viewModel.moveToNext();
                            }
                        } else if (finalDeltaX < -threshold) {
                            // Swipe left - DELETE
                            SwipeUiState state = viewModel.getUiState().getValue();
                            if (state != null && state.getCurrentIndex() < state.getPhotos().size()) {
                                PhotoItem photo = state.getPhotos().get(state.getCurrentIndex());
                                viewModel.decidePhoto(photo.getId(), PhotoDecision.DELETE);
                                viewModel.moveToNext();
                            }
                        }
                        
                        // Reset image position
                        imageView.animate()
                            .translationX(0)
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start();
                    }
                    
                    hideDragIndicators();
                    isDragging = false;
                    return true;
            }
            return false;
        });
    }
    
    private void updateDragIndicators(float deltaX) {
        if (deltaX > 0) {
            indicatorKeep.setVisibility(View.VISIBLE);
            indicatorDelete.setVisibility(View.GONE);
        } else if (deltaX < 0) {
            indicatorKeep.setVisibility(View.GONE);
            indicatorDelete.setVisibility(View.VISIBLE);
        } else {
            hideDragIndicators();
        }
    }
    
    private void hideDragIndicators() {
        indicatorKeep.setVisibility(View.GONE);
        indicatorDelete.setVisibility(View.GONE);
    }
    
    private void updateUI(SwipeUiState state) {
        if (state.getPhotos() == null || state.getPhotos().isEmpty()) return;
        
        int currentIndex = state.getCurrentIndex();
        if (currentIndex >= state.getPhotos().size()) {
            // Navigate to review
            navigateToReview();
            return;
        }
        
        PhotoItem photo = state.getPhotos().get(currentIndex);
        textProgress.setText((currentIndex + 1) + " / " + state.getPhotos().size());
        
        Glide.with(this)
            .load(photo.getUri())
            .into(imageView);
    }
    
    private void navigateToReview() {
        Bundle args = getArguments();
        if (args == null) return;
        
        String sourceType = args.getString(ARG_SOURCE_TYPE);
        String sourceId = args.getString(ARG_SOURCE_ID);
        
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        ReviewFragment reviewFragment = ReviewFragment.newInstance(sourceType, sourceId);
        transaction.replace(R.id.fragment_container, reviewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

