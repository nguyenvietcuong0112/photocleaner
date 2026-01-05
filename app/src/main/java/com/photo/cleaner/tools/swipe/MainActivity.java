package com.photo.cleaner.tools.swipe;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.photo.cleaner.tools.swipe.ui.screens.splash.SplashFragment;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "swipewipe_prefs";
    private static final String KEY_INTRO_SHOWN = "intro_shown";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SplashFragment());
            transaction.commit();
        }
    }
    
    public SharedPreferences getAppPreferences() {
        return getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }
    
    public boolean isIntroShown() {
        return getAppPreferences().getBoolean(KEY_INTRO_SHOWN, false);
    }
    
    public void setIntroShown(boolean shown) {
        getAppPreferences().edit().putBoolean(KEY_INTRO_SHOWN, shown).apply();
    }
}

