package com.advangelists.mediation;

import android.content.Context;
import android.widget.RelativeLayout;

import com.advangelists.ads.ADVAdErrorCode;
import com.advangelists.banner.ads.CustomEventBanner;
import com.advangelists.common.ADVAdSize;
import com.advangelists.common.ADVAdsUtils;
import com.advangelists.network.response.TypeParser;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiBanner.BannerAdListener;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.sdk.InMobiSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;



public class CustomInMobiBanner extends CustomEventBanner implements BannerAdListener {

    private InMobiBanner mInMobiBanner;
    private CustomEventBannerListener mCustomEventBannerListener;
    private Map<String, Object> mParams;

    public CustomInMobiBanner() {

    }

    @Override
    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> params, final ADVAdSize adSize, final ADVMediationAdRequest advMediationAdRequest, final Map<String, String> serverExtras) {
        mCustomEventBannerListener = customEventBannerListener;
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


        mInMobiBanner = new com.inmobi.ads.InMobiBanner(context, TypeParser.parseLong(params.get(MediationConstants.PLACEMENT_ID), 0L));
        mInMobiBanner.setBannerSize(adSize.getWidth(), adSize.getHeight());
        mInMobiBanner.setListener(this);
        if (advMediationAdRequest.getKeywords()!=null)
            mInMobiBanner.setKeywords(advMediationAdRequest.getKeywords());
        mInMobiBanner.load();

    }

    @Override
    public void onAdLoadSucceeded(com.inmobi.ads.InMobiBanner inMobiBanner) {
        if (mCustomEventBannerListener!=null){
            mCustomEventBannerListener.onBannerLoaded(inMobiBanner);
        }
    }

    @Override
    public void onAdLoadFailed(com.inmobi.ads.InMobiBanner inMobiBanner, InMobiAdRequestStatus inMobiAdRequestStatus) {
        if (mCustomEventBannerListener!=null){
            mCustomEventBannerListener.onBannerFailed(ADVAdErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public void onAdDisplayed(com.inmobi.ads.InMobiBanner inMobiBanner) {
        if (mCustomEventBannerListener!=null){
            mCustomEventBannerListener.onBannerDisplayed(inMobiBanner);
        }
    }

    @Override
    public void onAdDismissed(com.inmobi.ads.InMobiBanner inMobiBanner) {

    }

    @Override
    public void onAdInteraction(com.inmobi.ads.InMobiBanner inMobiBanner, Map<Object, Object> map) {
        if (mCustomEventBannerListener!=null){
            mCustomEventBannerListener.onBannerClicked();
        }
    }

    @Override
    public void onUserLeftApplication(com.inmobi.ads.InMobiBanner inMobiBanner) {
        if (mCustomEventBannerListener!=null){
            mCustomEventBannerListener.onLeaveApplication();
        }
    }

    @Override
    public void onAdRewardActionCompleted(com.inmobi.ads.InMobiBanner inMobiBanner, Map<Object, Object> map) {

    }

    @Override
    protected void onInvalidate() {
        mCustomEventBannerListener = null;
        mInMobiBanner.invalidate();
        mInMobiBanner = null;
    }

    @Override
    protected String getId() {
        if (mParams!=null && mParams.keySet().contains(MediationConstants.SDK_ID))
            return TypeParser.parseString(mParams.get(MediationConstants.SDK_ID), "");
        return "";
    }
}
