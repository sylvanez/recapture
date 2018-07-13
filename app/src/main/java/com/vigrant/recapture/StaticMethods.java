package com.vigrant.recapture;

import android.graphics.Bitmap;

class StaticMethods {

    static void setMainActivity(MainActivity mainActivity){
        sInstance.mMainActivity = mainActivity;
    }

    static void setOverlay(Overlay overlay){
        sInstance.mOverlay = overlay;
    }

    static void pickImage(){
        sInstance.mMainActivity.pickImage();
    }

    static void displayImage(Bitmap bitmap){
        sInstance.mOverlay.displayImage(bitmap);
    }

    static MainActivity mainActivity(){
        return sInstance.mMainActivity;
    }

    private MainActivity mMainActivity;
    private Overlay mOverlay;

    private static StaticMethods sInstance = new StaticMethods();

}

