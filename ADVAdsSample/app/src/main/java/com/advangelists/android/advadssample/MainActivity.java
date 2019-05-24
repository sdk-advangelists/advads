package com.advangelists.android.advadssample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.advangelists.ads.ADVAdErrorCode;
import com.advangelists.common.ADVAdRequest;
import com.advangelists.interstitial.ads.ADVAdInterstitial;
import com.advangelists.ads.ADVAdView;
import com.advangelists.interstitial.ads.ADVAdVideoInterstitial;
import com.advangelists.nativeads.ADVAdNative;
import com.advangelists.nativeads.ADVAdVideoNativeAdRenderer;
import com.advangelists.nativeads.BaseNativeAd;
import com.advangelists.nativeads.MediaViewBinder;
import com.advangelists.nativeads.NativeAd;
import com.advangelists.nativeads.NativeErrorCode;
import com.advangelists.nativeads.VideoConfiguration;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ADVAdView.ADVAdViewListener, ADVAdInterstitial.InterstitialAdListener, ADVAdVideoInterstitial.InterstitialVideoAdListener, ADVAdNative.ADVAdNativeAdListener {
    private ADVAdView mSmallBannerView;
    private ADVAdView mLargeBannerView;
    private ADVAdInterstitial mADVAdInterstitial;
    private ADVAdVideoInterstitial mADVAdVideoInterstitial;
    private ProgressBar mProgressBar;
    private SharedPreferences sharedPreferences;
    private Menu mMenu;
    private AlertDialog alert11;
    private MediaViewBinder mediaViewBinder;
    private ADVAdNative mADVAdNative;
    private NativeAd mNativeAd;
    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SettingsActivity.sharedPreferencesFileName, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isSettingsSaved", false)){
            String partnerId = "";
            try {
                partnerId = getResources().getText(getResources().getIdentifier("ADVADS_PARTNER_ID", "string", getPackageName())).toString();
            } catch (Exception e) {

            }
            String bundleId = getApplicationContext().getPackageName();
            try {
                bundleId = getResources().getText(getResources().getIdentifier("ADVADS_BUNDLE_ID", "string", getPackageName())).toString();
            } catch (Exception e) {

            }
            String cat = "";
            try {
                cat = getResources().getText(getResources().getIdentifier("ADVADS_CAT", "string", getPackageName())).toString();
            } catch (Exception e) {

            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("partnerId", partnerId);
            editor.putString("bundleId", bundleId);
            editor.putString("placementId", "");
            editor.putString("cat", cat);
            editor.putBoolean("isSettingsSaved", true);
            editor.commit();

        }
        setContentView(R.layout.activity_main);
        mSmallBannerView = findViewById(R.id.smallBannerView);
        mLargeBannerView = findViewById(R.id.largeBannerView);
        mProgressBar = findViewById(R.id.progressBar2);
        mSmallBannerView.setADVAdViewListener(this);
        mLargeBannerView.setADVAdViewListener(this);
        mSmallBannerView.setLocationAutoUpdateEnabled(true);
        mLargeBannerView.setLocationAutoUpdateEnabled(true);
        mLargeBannerView.setAdAutoRefreshEnabled(true);     //Default value is false
        mSmallBannerView.setAdAutoRefreshEnabled(true);
        mLargeBannerView.setTesting(true);
        mSmallBannerView.setTesting(true);
        mLargeBannerView.setBundleIdentifier(sharedPreferences.getString("bundleId", ""));
        mLargeBannerView.setCategory(sharedPreferences.getString("cat", ""));
        mSmallBannerView.setBundleIdentifier(sharedPreferences.getString("bundleId", ""));
        mSmallBannerView.setCategory(sharedPreferences.getString("cat", ""));

        mConstraintLayout = findViewById(R.id.native_video_ad_space);

        mediaViewBinder = new MediaViewBinder.Builder(R.layout.native_video_ad_layout)
                .mediaLayoutId(R.id.native_ad_video_view)
                .iconImageId(R.id.native_ad_icon_image)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_text)
                .build();



    }

    public void loadInterstitialVideo(View v){
        clearAllViews();
        if (mADVAdVideoInterstitial == null){
            mADVAdVideoInterstitial = new ADVAdVideoInterstitial(this);
            mADVAdVideoInterstitial.setLocationAutoUpdateEnabled(true);
            mADVAdVideoInterstitial.setInterstitialVideoAdListener(this);
            mADVAdVideoInterstitial.setTesting(true);
            mADVAdVideoInterstitial.setBundleIdentifier(sharedPreferences.getString("bundleId", ""));
            mADVAdVideoInterstitial.setCategory(sharedPreferences.getString("cat", ""));
        }
        HashMap<String, String > map = new HashMap<>();
        map.put("gender", "male");
        map.put("age", "25");
        map.put("income", "100000");
        map.put("language", "en");
        VideoConfiguration videoConfiguration = new VideoConfiguration.Builder().build(this);
        mADVAdVideoInterstitial.requestInterstitialVideo(this,videoConfiguration, map, sharedPreferences.getString("partnerId", ""), sharedPreferences.getString("placementId", ""));
    }

    public void loadNativeVideo(View v){
        mProgressBar.setVisibility(View.VISIBLE);
        dismissIfAlertDialogVisible();
        clearAllViews();
        ADVAdVideoNativeAdRenderer advAdVideoNativeAdRenderer = new ADVAdVideoNativeAdRenderer(mediaViewBinder);
        mADVAdNative = new ADVAdNative(this, this);
        mADVAdNative.registerAdRenderer(advAdVideoNativeAdRenderer);
        ADVAdRequest advAdRequest = (new ADVAdRequest.Builder()).build(this);
        advAdRequest.bundleId = sharedPreferences.getString("bundleId", "");
        advAdRequest.partnerId = sharedPreferences.getString("partnerId", "");
        advAdRequest.placementId = sharedPreferences.getString("placementId", "");
        advAdRequest.cat = sharedPreferences.getString("cat", "");
        advAdRequest.testing = true;
        mADVAdNative.makeRequest(advAdRequest);
    }

    public void loadInterstitial(View v){
        clearAllViews();
        if (mADVAdInterstitial == null){
            mADVAdInterstitial = new ADVAdInterstitial(this);
            mADVAdInterstitial.setLocationAutoUpdateEnabled(true);
            mADVAdInterstitial.setInterstitialAdListener(this);
            mADVAdInterstitial.setBundleIdentifier(sharedPreferences.getString("bundleId", ""));
            mADVAdInterstitial.setCategory(sharedPreferences.getString("cat", ""));
            mADVAdInterstitial.setTesting(true);
        }
        HashMap<String, String > map = new HashMap<>();
        map.put("gender", "male");
        map.put("age", "25");
        map.put("income", "100000");
        map.put("language", "en");
        mADVAdInterstitial.requestNewAd(this,map, sharedPreferences.getString("partnerId", ""), sharedPreferences.getString("placementId", ""));
    }

    private void clearAllViews(){
        if (mNativeAd!=null){
            mNativeAd.clear(mConstraintLayout);
            mConstraintLayout.setVisibility(View.INVISIBLE);
        }
        mLargeBannerView.setVisibility(View.INVISIBLE);
        mLargeBannerView.setAdAutoRefreshEnabled(false);
        mSmallBannerView.setVisibility(View.INVISIBLE);
        mSmallBannerView.setAdAutoRefreshEnabled(false);

        if (mADVAdInterstitial!=null){
            mADVAdInterstitial.destroy();
            mADVAdInterstitial = null;
        }

        if (mADVAdVideoInterstitial!=null){
            mADVAdVideoInterstitial.destroy();
            mADVAdVideoInterstitial = null;
        }
    }

    public void loadSmallBanner(View v){
        clearAllViews();
        HashMap<String, String > map = new HashMap<>();
        map.put("gender", "male");
        map.put("age", "25");
        map.put("income", "100000");
        map.put("language", "en");
        mSmallBannerView.setAdAutoRefreshEnabled(true);
        mSmallBannerView.requestNewAd(map, sharedPreferences.getString("partnerId", ""), sharedPreferences.getString("placementId", ""));

    }

    public void loadLargeBanner(View v){
        clearAllViews();
        HashMap<String, String > map = new HashMap<>();
        map.put("gender", "male");
        map.put("age", "25");
        map.put("income", "100000");
        map.put("language", "en");
        mLargeBannerView.setAdAutoRefreshEnabled(true);
        mLargeBannerView.requestNewAd(map, sharedPreferences.getString("partnerId", ""), sharedPreferences.getString("placementId", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.settings).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInterstitialAdRequest(ADVAdInterstitial advAdInterstitial) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInterstitialLoaded(ADVAdInterstitial advAdInterstitial) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (advAdInterstitial.isReady()) {
            advAdInterstitial.show();
            dismissIfAlertDialogVisible();
        }


    }

    @Override
    public void onInterstitialFailed(ADVAdInterstitial advAdInterstitial, ADVAdErrorCode advAdErrorCode) {
        mProgressBar.setVisibility(View.INVISIBLE);
        showAlertDialogWithMessage("Failed to load Interstitial Ad");
    }

    @Override
    public void onInterstitialShown(ADVAdInterstitial advAdInterstitial) {
        Log.d("","onInterstitialShown");
    }

    @Override
    public void onInterstitialClicked(ADVAdInterstitial advAdInterstitial) {
        Log.d("","onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(ADVAdInterstitial advAdInterstitial) {
        Log.d("","onInterstitialDismissed");
    }

    @Override
    public void onInterstitialVideoAdRequest(final ADVAdVideoInterstitial videoInterstitial) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInterstitialVideoLoaded(final ADVAdVideoInterstitial videoInterstitial) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (videoInterstitial.isReady()) {
            videoInterstitial.show();
            dismissIfAlertDialogVisible();
        }
    }

    @Override
    public void onInterstitialVideoFailed(final ADVAdVideoInterstitial videoInterstitial, final ADVAdErrorCode errorCode) {
        mProgressBar.setVisibility(View.INVISIBLE);
        showAlertDialogWithMessage("Failed to load Interstitial Video Ad");
    }

    @Override
    public void onInterstitialVideoShown(final ADVAdVideoInterstitial videoInterstitial) {
        Log.i("","onInterstitialVideoShown");
    }

    @Override
    public void onInterstitialVideoClicked(final ADVAdVideoInterstitial videoInterstitial) {
        Log.i("","onInterstitialVideoClicked");
    }

    @Override
    public void onInterstitialVideoDismissed(final ADVAdVideoInterstitial videoInterstitial) {
        Log.i("","onInterstitialVideoDismissed");
    }

    @Override
    public void onInterstitialVideoEnded(final ADVAdVideoInterstitial videoInterstitial) {
        Log.i("","onInterstitialVideoEnded");
    }

    @Override
    public void onBannerAdRequest(ADVAdView advAdView) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBannerLoaded(ADVAdView advAdView) {
        mProgressBar.setVisibility(View.INVISIBLE);
        advAdView.setVisibility(View.VISIBLE);
        dismissIfAlertDialogVisible();
    }

    @Override
    public void onBannerFailed(ADVAdView advAdView, ADVAdErrorCode advAdErrorCode) {
        mProgressBar.setVisibility(View.INVISIBLE);
        showAlertDialogWithMessage("Failed to receive "+(advAdView==mLargeBannerView?"300x250":"320x50")+" Ad");
    }

    @Override
    public void onBannerClicked(ADVAdView advAdView) {

    }

    @Override
    public void onBannerExpanded(ADVAdView advAdView) {

    }

    @Override
    public void onBannerCollapsed(ADVAdView advAdView) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mSmallBannerView!=null){
            mSmallBannerView.destroy();
            mSmallBannerView = null;
        }
        if (mLargeBannerView!=null){
            mLargeBannerView.destroy();
            mLargeBannerView = null;
        }

        clearAllViews();
        super.onDestroy();
    }

    private void dismissIfAlertDialogVisible(){
        if (alert11 != null && alert11.isShowing()){
            alert11.cancel();
        }
    }

    private void showAlertDialogWithMessage(String message){
        dismissIfAlertDialogVisible();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onNativeLoad(final NativeAd nativeAd) {
        mProgressBar.setVisibility(View.INVISIBLE);
        dismissIfAlertDialogVisible();
        View adView = nativeAd.createAdView(MainActivity.this, mConstraintLayout);
        mNativeAd = nativeAd;
        nativeAd.prepare(adView);
        nativeAd.renderAdView(adView);
        mConstraintLayout.addView(adView);
        mConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNativeFail(final NativeErrorCode errorCode) {
        mProgressBar.setVisibility(View.INVISIBLE);
        showAlertDialogWithMessage("Failed to load Native Video Ad");
    }

    @Override
    public void onNativeVideoEnded(final BaseNativeAd nativeAd) {
        Log.i("","onNativeVideoEnded");
    }

    @Override
    public void onNativeClicked(final BaseNativeAd baseNativeAd) {

    }

    @Override
    public boolean isRewardedVideo(ADVAdVideoInterstitial advAdVideoInterstitial) {
        return true;
    }

    @Override
    public void onInterstitialRewardsUnlocked(ADVAdVideoInterstitial advAdVideoInterstitial, Map<Object, Object> map) {

    }

}
