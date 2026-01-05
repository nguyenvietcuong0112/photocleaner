package com.photo.cleaner.tools.swipe.ui.screens.splash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.photo.cleaner.tools.swipe.MainActivity;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.ui.screens.intro.IntroFragment;
import com.photo.cleaner.tools.swipe.ui.screens.permission.PermissionFragment;
import com.photo.cleaner.tools.swipe.ui.screens.home.HomeFragment;

public class SplashFragment extends Fragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity == null) return;
            
            // Check permission
            boolean hasPermission = checkPermissions();
            
            if (!hasPermission) {
                navigateToPermission();
            } else if (!activity.isIntroShown()) {
                navigateToIntro();
            } else {
                navigateToHome();
            }
        }, 800);
    }
    
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    
    private void navigateToIntro() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new IntroFragment());
        transaction.commit();
    }
    
    private void navigateToPermission() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PermissionFragment());
        transaction.commit();
    }
    
    private void navigateToHome() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.commit();
    }
}

