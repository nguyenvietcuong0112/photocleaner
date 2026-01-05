package com.photo.cleaner.tools.swipe.ui.screens.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.photo.cleaner.tools.swipe.data.repository.PhotoRepository;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {
    private final PhotoRepository photoRepository;
    private final MutableLiveData<HomeUiState> _uiState = new MutableLiveData<>(new HomeUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public HomeViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public LiveData<HomeUiState> getUiState() {
        return _uiState;
    }

    public void loadData() {
        executor.execute(() -> {
            Calendar today = Calendar.getInstance();
            com.photo.cleaner.tools.swipe.data.OnThisDayData onThisDay = photoRepository.getOnThisDayData(
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.MONTH) + 1
            );

            List<com.photo.cleaner.tools.swipe.data.MonthProgress> months = photoRepository.getMonthlyProgress();

            HomeUiState newState = _uiState.getValue().copy(onThisDay, months);
            _uiState.postValue(newState);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

