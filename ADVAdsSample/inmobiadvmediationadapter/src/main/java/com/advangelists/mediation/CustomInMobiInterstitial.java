package com.advangelists.mediation;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.advangelists.ads.ADVAdErrorCode;
import com.advangelists.common.ADVAdsUtils;
import com.advangelists.interstitial.ads.CustomEventInterstitial;
import com.advangelists.interstitial.ads.CustomEventInterstitial.CustomEventInterstitialListener;
import com.advangelists.network.response.TypeParser;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;
import com.inmobi.sdk.InMobiSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomInMobiInterstitial extends CustomEventInterstitial {

    private CustomEventInterstitialListener mCustomEventInterstitialListener;
    private InMobiInterstitial mInterstitialAd;
    private final String TAG = CustomInMobiInterstitial.class.getSimpleName();
    private Map<String, Object> mParams;

    @Override
    protected void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> params, ADVMediationAdRequest advMediationAdRequest, HashMap<String, String> serverExtras) {
        mCustomEventInterstitialListener = customEventInterstitialListener;
        mParams = params;
        JSONObject consent = new JSONObject();
        try {
            // Provide correct consent value to sdk which is obtained by User
            consent.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, ADVAdsUtils.isConsentProvided());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(context, TypeParser.parseString(params.get(MediationConstants.PUBLISHER_ID), ""), consent);

        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

        int age = TypeParser.parseInteger(advMediationAdRequest.getAge(), -1);
        if (age>0){
            if (age<18)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BELOW_18);
            else if (age>=18 && age<25)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_18_AND_24);
            else if (age>=25 && age<30)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_25_AND_29);
            else if (age>=30 && age<35)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_30_AND_34);
            else if (age>=35 && age<45)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_35_AND_44);
            else if (age>=45 && age<55)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_45_AND_54);
            else if (age>=55 && age<66)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.BETWEEN_55_AND_65);
            else if (age>=66)
                InMobiSdk.setAgeGroup(InMobiSdk.AgeGroup.ABOVE_65);
        }

//        InMobiSdk.setEducation(InMobiSdk.Education.valueOf(advMediationAdRequest.getEducation()));

        if (advMediationAdRequest.getGender().equals("M"))
            InMobiSdk.setGender(InMobiSdk.Gender.MALE);
        else if (advMediationAdRequest.getGender().equals("F"))
            InMobiSdk.setGender(InMobiSdk.Gender.FEMALE);

        mInterstitialAd = new InMobiInterstitial(context.getApplicationContext(), TypeParser.parseLong(params.get(MediationConstants.PLACEMENT_ID), 0L),
                new InterstitialAdEventListener() {
                    @Override
                    public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
                        super.onAdLoadSucceeded(inMobiInterstitial);
                        Log.d(TAG, "onAdLoadSuccessful");
                        if (inMobiInterstitial.isReady() && mCustomEventInterstitialListener!=null) {
                            mCustomEventInterstitialListener.onInterstitialLoaded();
                        } else {
                            Log.d(TAG, "onAdLoadSuccessful inMobiInterstitial not ready");
                        }
                    }

                    @Override
                    public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onInterstitialFailed(ADVAdErrorCode.INTERNAL_ERROR);
                        super.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus);
                        Log.d(TAG, "Unable to load interstitial ad (error message: " +
                                inMobiAdRequestStatus.getMessage());
                    }

                    @Override
                    public void onAdReceived(InMobiInterstitial inMobiInterstitial) {
                        super.onAdReceived(inMobiInterstitial);
                        Log.d(TAG, "onAdReceived");
                    }

                    @Override
                    public void onAdClicked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onInterstitialClicked();
                        super.onAdClicked(inMobiInterstitial, map);
                        Log.d(TAG, "onAdClicked " + map.size());
                    }

                    @Override
                    public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {
                        super.onAdWillDisplay(inMobiInterstitial);
                        Log.d(TAG, "onAdWillDisplay " + inMobiInterstitial);
                    }

                    @Override
                    public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onInterstitialShown();
                        super.onAdDisplayed(inMobiInterstitial);
                        Log.d(TAG, "onAdDisplayed " + inMobiInterstitial);
                    }

                    @Override
                    public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onInterstitialFailed(ADVAdErrorCode.VIDEO_PLAYBACK_ERROR);
                        super.onAdDisplayFailed(inMobiInterstitial);
                        Log.d(TAG, "onAdDisplayFailed " + "FAILED");
                    }

                    @Override
                    public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onInterstitialDismissed();
                        super.onAdDismissed(inMobiInterstitial);
                        Log.d(TAG, "onAdDismissed " + inMobiInterstitial);
                    }

                    @Override
                    public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onLeaveApplication();
                        super.onUserLeftApplication(inMobiInterstitial);
                        Log.d(TAG, "onUserWillLeaveApplication " + inMobiInterstitial);
                    }

                    @Override
                    public void onRewardsUnlocked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                        if (mCustomEventInterstitialListener!=null)
                            mCustomEventInterstitialListener.onRewardsUnlocked(map);
                        super.onRewardsUnlocked(inMobiInterstitial, map);
                        Log.d(TAG, "onRewardsUnlocked " + map.size());
                    }
                });

        if (advMediationAdRequest.getKeywords()!=null)
            mInterstitialAd.setKeywords(advMediationAdRequest.getKeywords());

        mInterstitialAd.load();

    }

    @Override
    protected void showInterstitial() {
        if (mInterstitialAd!=null && mInterstitialAd.isReady()){
            mInterstitialAd.show();
        }else {
            Log.d(TAG, "onShowInterstitial inMobiInterstitial not ready");
        }
    }

    @Override
    protected void onInvalidate() {
        mCustomEventInterstitialListener = null;
        if (mInterstitialAd!=null){
            mInterstitialAd.disableHardwareAcceleration();
            mInterstitialAd = null;
        }
    }

    @Override
    protected String getId() {
        if (mParams!=null && mParams.keySet().contains(MediationConstants.SDK_ID))
            return TypeParser.parseString(mParams.get(MediationConstants.SDK_ID), "");
        return "";
    }
}
