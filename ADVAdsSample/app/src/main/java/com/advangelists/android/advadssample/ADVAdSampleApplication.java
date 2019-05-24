package com.advangelists.android.advadssample;

import android.app.Application;

import com.advangelists.common.ADVAdsUtils;

import java.util.logging.Level;


/**
 * Created by arungupta on 11/01/18.
 */

public class ADVAdSampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ADVAdsUtils.initialize(this);
        ADVAdsUtils.setLogLevel(Level.ALL);
    }
}
