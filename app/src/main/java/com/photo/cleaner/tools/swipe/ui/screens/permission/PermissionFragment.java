package com.photo.cleaner.tools.swipe.ui.screens.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.photo.cleaner.tools.swipe.R;
import com.photo.cleaner.tools.swipe.ui.screens.home.HomeFragment;

public class PermissionFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private TextView textDescription;
    private Button btnGrant;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        textDescription = view.findViewById(R.id.textDescription);
        btnGrant = view.findViewById(R.id.btnGrant);
        
        if (checkPermissions()) {
            navigateToHome();
            return;
        }
        
        btnGrant.setOnClickListener(v -> {
            if (shouldShowRequestPermissionRationale()) {
                openSettings();
            } else {
                requestPermissions();
            }
        });
    }
    
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    
    private boolean shouldShowRequestPermissionRationale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            return shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            }, PERMISSION_REQUEST_CODE);
        } else {
            requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }
    
    private void openSettings() {
        textDescription.setText(R.string.permission_denied);
        btnGrant.setText(R.string.permission_settings);
        
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToHome();
            } else {
                textDescription.setText(R.string.permission_denied);
                btnGrant.setText(R.string.permission_settings);
            }
        }
    }
    
    private void navigateToHome() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.commit();
    }
}

