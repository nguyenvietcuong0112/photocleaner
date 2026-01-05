package com.photo.cleaner.tools.swipe.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProgressStorageImpl implements ProgressStorage {
    private final SharedPreferences prefs;
    
    public ProgressStorageImpl(Context context) {
        this.prefs = context.getSharedPreferences("swipewipe_progress", Context.MODE_PRIVATE);
    }
    
    @Override
    public void saveProgress(String sessionId, Map<String, String> photoDecisions) {
        try {
            JSONObject json = new JSONObject();
            for (Map.Entry<String, String> entry : photoDecisions.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
            prefs.edit().putString(sessionId, json.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Map<String, String> getProgress(String sessionId) {
        String jsonString = prefs.getString(sessionId, null);
        if (jsonString == null) {
            return new HashMap<>();
        }
        
        try {
            JSONObject json = new JSONObject(jsonString);
            Map<String, String> map = new HashMap<>();
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, json.getString(key));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    @Override
    public void clearProgress(String sessionId) {
        prefs.edit().remove(sessionId).apply();
    }
}

