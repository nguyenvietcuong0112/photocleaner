package com.photo.cleaner.tools.swipe.ui.screens.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.photo.cleaner.tools.swipe.R;

public class IntroPageFragment extends Fragment {
    private static final String ARG_PAGE = "page";
    
    public static IntroPageFragment newInstance(int page) {
        IntroPageFragment fragment = new IntroPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_page, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TextView textView = view.findViewById(R.id.textTitle);
        int page = getArguments() != null ? getArguments().getInt(ARG_PAGE, 0) : 0;
        
        String[] titles = {
            getString(R.string.intro_title_1),
            getString(R.string.intro_title_2),
            getString(R.string.intro_title_3)
        };
        
        textView.setText(titles[page]);
    }
}

