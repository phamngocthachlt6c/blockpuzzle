package com.thachpham.blockpuzzlerockstone.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager sResourceManager;

    private Context mContext;
    private Map<String, Bitmap> bitmapMap;

    private final int MAX_STREAMS = 5;
    private SoundPool soundPool;
    private AudioManager audioManager;
    private boolean loaded;
    private int buttonClickSoundId;
    private int characterFlySoundId;
    private int goldSound;
    private float volume;

    private int goldSoundStream = 0;

    public static ResourceManager getInstance(Context context) {
        if (sResourceManager == null) {
            sResourceManager = new ResourceManager(context);
        }
        return sResourceManager;
    }

    public static ResourceManager getInstance() {
        return sResourceManager;
    }

    private ResourceManager(Context context) {
        mContext = context;
        bitmapMap = new HashMap<>();
    }

    public void loadData() {
        try {

            // Skin5
            Bitmap bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/stone.png");
            bitmapMap.put("stone", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/tiled_stone_ground.png");
            bitmapMap.put("tiled_stone_ground", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/header_background.png");
            bitmapMap.put("header_background", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/footer_background.png");
            bitmapMap.put("footer_background", bitmap);
            bitmap = FunctionUtil.getBitmapFromAsset(mContext, "drawable/tutorial_hand.png");
            bitmapMap.put("tutorial_hand", bitmap);

//            loadSound();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadSound() {
//        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//        float currentVolumeIndex = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        float maxVolumeIndex = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//
//        volume = currentVolumeIndex / maxVolumeIndex;
//        ((MainActivity) mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//        if (Build.VERSION.SDK_INT >= 21) {
//
//            AudioAttributes audioAttrib = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_GAME)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build();
//
//            SoundPool.Builder builder = new SoundPool.Builder();
//            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
//
//            this.soundPool = builder.build();
//        } else {
//            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
//        }
//
//        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                loaded = true;
//            }
//        });
//
//        this.buttonClickSoundId = this.soundPool.load(mContext, R.raw.button_click_sound, 1);
//        this.characterFlySoundId = this.soundPool.load(mContext, R.raw.character_fly, 1);
//        this.goldSound = this.soundPool.load(mContext, R.raw.gold_sound, 1);
    }

    public Bitmap getBitmap(String name) {
        return bitmapMap.get(name);
    }

//    public void playButtonClickSound() {
//        if (loaded) {
//            float leftVolumn = volume;
//            float rightVolumn = volume;
//            try {
//                int streamId = this.soundPool.play(this.buttonClickSoundId, leftVolumn, rightVolumn, 1, 0, 1f);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public void playCharacterFlySound() {
//        if (loaded) {
//            float leftVolumn = volume;
//            float rightVolumn = volume;
//            try {
//                int streamId = this.soundPool.play(this.characterFlySoundId, leftVolumn, rightVolumn, 1, 0, 1f);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void playGoldSound() {
//        if (loaded) {
//            float leftVolumn = volume;
//            float rightVolumn = volume;
//            try {
//                if(AudioS)
//                int streamId = this.soundPool.play(this.goldSound, leftVolumn, rightVolumn, 1, 0, 1f);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
