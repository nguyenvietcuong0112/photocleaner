package com.photo.cleaner.tools.swipe.navigation;

public class Screen {
    public static final String SPLASH = "splash";
    public static final String INTRO = "intro";
    public static final String PERMISSION = "permission";
    public static final String HOME = "home";
    
    public static String createSwipeRoute(String sourceType, String sourceId) {
        return "swipe/" + sourceType + "/" + sourceId;
    }
    
    public static String createReviewRoute(String sourceType, String sourceId) {
        return "review/" + sourceType + "/" + sourceId;
    }
}

