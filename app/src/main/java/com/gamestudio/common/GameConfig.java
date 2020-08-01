package com.gamestudio.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {

    private final String PREFERENCE_NAME = "config_preference";
    private final String HIGH_SCORE_FIELD = "high_score";
    private final String SKIN_DATA = "skin_data";
    private final String GOLD_FIELD = "gold";
    private final String SOUND_ON = "sound_on";
    private final String TUTORIAL_STEP = "tutorial_step";
    private static GameConfig sInstance;
    private SharedPreferences sharedPreferences;

    private boolean soundAvailable;
    private int highScore;
    private int gold;
    private int tutorialStep;
    private SkinData skinData;

    private Gson gson;

    public static GameConfig getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GameConfig(context);
        }
        return sInstance;
    }

    public void loadConfig() {
        highScore = sharedPreferences.getInt(HIGH_SCORE_FIELD, 0);
        gold = sharedPreferences.getInt(GOLD_FIELD, 0);
        soundAvailable = sharedPreferences.getBoolean(SOUND_ON, true);
        tutorialStep = sharedPreferences.getInt(TUTORIAL_STEP, 1);

        String skinJson = sharedPreferences.getString(SKIN_DATA, null);
        if (skinJson == null) {
            skinData = new SkinData();
            skinData.setCurrentSkinId(0);
            List<Integer> listIdOwned = new ArrayList<>();
            listIdOwned.add(0);
            skinData.setListSkinOwned(listIdOwned);
        } else {
            skinData = gson.fromJson(skinJson, SkinData.class);
        }
    }

    public static GameConfig getInstance() {
        return sInstance;
    }

    private GameConfig(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public void saveHighScore(int highScore) {
        this.highScore = highScore;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGH_SCORE_FIELD, highScore);
        editor.apply();
    }

    public int getHighScore() {
        return highScore;
    }

    public void saveGold(int gold) {
        this.gold = gold;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GOLD_FIELD, gold);
        editor.apply();
    }

    public void saveTutorialStep(int step) {
        this.tutorialStep = step;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TUTORIAL_STEP, step);
        editor.apply();
    }

    public int getTutorialStep() {
        return tutorialStep;
    }

    public void saveSkinData(SkinData skinData) {
        this.skinData = skinData;
        String jsonString = gson.toJson(skinData);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SKIN_DATA, jsonString);
        editor.apply();
    }

    public SkinData getSkinData() {
        return skinData;
    }

    public int getGold() {
        return gold;
    }

    public boolean getSoundAvailable() {
        return soundAvailable;
    }

    public void setSoundAvailable(boolean available) {
        soundAvailable = available;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND_ON, available);
        editor.apply();
    }

    public class SkinData {
        private int currentSkinId;
        private List<Integer> listSkinOwned;

        public int getCurrentSkinId() {
            return currentSkinId;
        }

        public void setCurrentSkinId(int currentSkinId) {
            this.currentSkinId = currentSkinId;
        }

        public List<Integer> getListSkinOwned() {
            return listSkinOwned;
        }

        public void setListSkinOwned(List<Integer> listSkinOwned) {
            this.listSkinOwned = listSkinOwned;
        }

        public void addSkinOwned(Integer id) {
            if (!FunctionUtil.existIdInList(id, listSkinOwned)) {
                listSkinOwned.add(id);
            }
        }
    }

}
