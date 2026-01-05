package com.photo.cleaner.tools.swipe.ui.screens.home;

import com.photo.cleaner.tools.swipe.data.MonthProgress;
import com.photo.cleaner.tools.swipe.data.OnThisDayData;

import java.util.ArrayList;
import java.util.List;

public class HomeUiState {
    private OnThisDayData onThisDay;
    private List<MonthProgress> monthlyProgress;

    public HomeUiState() {
        this(null, new ArrayList<>());
    }

    public HomeUiState(OnThisDayData onThisDay, List<MonthProgress> monthlyProgress) {
        this.onThisDay = onThisDay;
        this.monthlyProgress = monthlyProgress != null ? monthlyProgress : new ArrayList<>();
    }

    public OnThisDayData getOnThisDay() {
        return onThisDay;
    }

    public void setOnThisDay(OnThisDayData onThisDay) {
        this.onThisDay = onThisDay;
    }

    public List<MonthProgress> getMonthlyProgress() {
        return monthlyProgress;
    }

    public void setMonthlyProgress(List<MonthProgress> monthlyProgress) {
        this.monthlyProgress = monthlyProgress;
    }

    public HomeUiState copy(OnThisDayData onThisDay, List<MonthProgress> monthlyProgress) {
        return new HomeUiState(onThisDay, monthlyProgress);
    }
}

