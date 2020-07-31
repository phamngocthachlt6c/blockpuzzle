package com.thachpham.blockpuzzlerockstone;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.thachpham.blockpuzzle_rockstone.R;
import com.thachpham.blockpuzzle_rockstone.databinding.ActivityMainBinding;
import com.thachpham.blockpuzzlerockstone.common.NetworkUtil;
import com.thachpham.blockpuzzlerockstone.common.ResourceManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int BANNER_ADS_SHOWED_FOR_VIDEO_ADS = 5;
    private static final int BANNER_ADS_RELOAD_AFTER = 3;
    private int numberOfBannerAdsShowed;

    private RewardedVideoAd mRewardedVideoAd;
    private boolean videoAdsReadyToShow;
    private InterstitialAd mInterstitialAd;
    private boolean interstiReadyToShow;
    private int bannerNumberShowBeforeReload;

    private Random random;
    private ActivityMainBinding binding;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
//                Log.d("aaa", "onReceive: network not connected");
                } else {
                    loadAdsWhenInternetConnected();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceManager resourceManager = ResourceManager.getInstance(this);
        resourceManager.loadData();
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Initializing for all ads
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // For Banner
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.bannerAds.loadAd(adRequest);

        // For video ads
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        mRewardedVideoAd.loadAd(getString(R.string.video_load_ad_unit_id), new AdRequest.Builder().build());

        // For InterstitialAd
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(interstitialAdListener);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        random = new Random();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
//        binding.mGameView.continueGameByActivityCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(this);
//        binding.mGameView.pauseGameByActivityCycle();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        unregisterReceiver(broadcastReceiver);
        binding.mGameView.destroyThread();
        super.onDestroy();
    }


    public void showAds() {
        showBannerAds();
        if (isShowFullAds()) {
            if (isShowInterstitialAd()) {
                showInitialAds();
            } else {
                showVideoAds();
            }
        }
    }

    public void hideAds() {
        binding.bannerAds.setVisibility(View.GONE);
    }

    private boolean isShowFullAds() {
        return numberOfBannerAdsShowed >= BANNER_ADS_SHOWED_FOR_VIDEO_ADS;
    }

    private boolean isShowInterstitialAd() {
        return random.nextFloat() > 0.3f;
    }

    private void showBannerAds() {
        //show every time go to screen game over, except show video ads
        binding.bannerAds.setVisibility(View.VISIBLE);
        numberOfBannerAdsShowed++;
        bannerNumberShowBeforeReload++;
//        if (bannerNumberShowBeforeReload >= BANNER_ADS_RELOAD_AFTER) {
//            bannerNumberShowBeforeReload = 0;
//            try {
//                AdRequest adRequest = new AdRequest.Builder().build();
//                binding.bannerAds.loadAd(adRequest);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void showVideoAds() {
        if (videoAdsReadyToShow) {
            mRewardedVideoAd.show();
            videoAdsReadyToShow = false;
        } else {
            numberOfBannerAdsShowed = BANNER_ADS_SHOWED_FOR_VIDEO_ADS - 1;
        }
    }

    private void showInitialAds() {
        if (interstiReadyToShow) {
            mInterstitialAd.show();
            interstiReadyToShow = false;
        } else {
            numberOfBannerAdsShowed = BANNER_ADS_SHOWED_FOR_VIDEO_ADS - 1;
        }
    }

    public void loadAdsWhenInternetConnected() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.bannerAds.loadAd(adRequest);
        // For video ads
        mRewardedVideoAd.loadAd(getString(R.string.video_load_ad_unit_id), new AdRequest.Builder().build());

        // For InterstitialAd
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            videoAdsReadyToShow = true;
        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {
            videoAdsReadyToShow = false;
            mRewardedVideoAd.loadAd(getString(R.string.video_load_ad_unit_id), new AdRequest.Builder().build());
            numberOfBannerAdsShowed = 0;
        }

        @Override
        public void onRewarded(RewardItem rewardItem) {

        }

        @Override
        public void onRewardedVideoAdLeftApplication() {

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {

        }

        @Override
        public void onRewardedVideoCompleted() {
//            binding.mGameView.plusGold(20);
//            informDialog.show("You received 20 golds!");
        }
    };

    private AdListener interstitialAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            interstiReadyToShow = true;
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            interstiReadyToShow = false;
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            numberOfBannerAdsShowed = 0;
        }
    };

    public void openLinkRatingApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
