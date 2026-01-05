package com.photo.cleaner.tools.swipe.ui.screens.swipe;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.photo.cleaner.tools.swipe.data.repository.PhotoLoader;
import com.photo.cleaner.tools.swipe.data.repository.PhotoLoaderImpl;
import com.photo.cleaner.tools.swipe.data.repository.PhotoRepository;
import com.photo.cleaner.tools.swipe.data.repository.PhotoRepositoryImpl;
import com.photo.cleaner.tools.swipe.data.repository.ProgressStorage;
import com.photo.cleaner.tools.swipe.data.repository.ProgressStorageImpl;

public class SwipeViewModelFactory implements ViewModelProvider.Factory {
    private final String sourceType;
    private final String sourceId;
    private final Context context;

    public SwipeViewModelFactory(String sourceType, String sourceId, Context context) {
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        PhotoLoader photoLoader = new PhotoLoaderImpl(context);
        ProgressStorage progressStorage = new ProgressStorageImpl(context);
        PhotoRepository photoRepository = new PhotoRepositoryImpl(photoLoader, progressStorage);
        return (T) new SwipeViewModel(photoRepository, sourceType, sourceId);
    }
}

