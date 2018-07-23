package com.vigrant.recapture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Overlay extends Service {


    private WindowManager mWindowManager;
    private LinearLayout mLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        StaticMethods.setOverlay(this);

        mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        mLayout = new LinearLayout(this);
        StaticMethods.mainActivity().getLayoutInflater().inflate(R.layout.overlay, mLayout);


        int LAYOUT_TYPE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mLayoutParamsWM = new WindowManager.LayoutParams(LAYOUT_TYPE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
        mLayoutParamsWM.x = 0;
        mLayoutParamsWM.y = 0;
        mLayoutParamsWM.gravity = Gravity.CENTER;

        mWindowManager.addView(mLayout, mLayoutParamsWM);
        StaticMethods.mainActivity().onOverlayStarted();
    }

    void displayImage(Bitmap bitmap){
        //((ImageView)mLayout.findViewById(R.id.overlay_image)).setImageBitmap(bitmap);
        if(null != mImageZoom){
            ((FrameLayout)mLayout.findViewById(R.id.overlay_layout_main)).removeView(mImageZoom);
        }

        mImageZoom = new ImageZoom(bitmap);
        ((FrameLayout)mLayout.findViewById(R.id.overlay_layout_main)).addView(mImageZoom);


        System.out.println("image of size " + bitmap.getWidth() + "x" + bitmap.getHeight() + " set");
        mLayout.invalidate();
    }

    void stop(){
        mWindowManager.removeView(mLayout);
        stopSelf();
    }

    void rotateLeft(){
        mImageZoom.rotateLeft();
    }

    void rotateRight(){
        mImageZoom.rotateRight();
    }

    void left(){
        mPaddingLeft -= 10;
        mLayout.setPadding(mPaddingLeft, 0, 0, 0);
    }

    void right(){
        mPaddingLeft += 10;
        mLayout.setPadding(mPaddingLeft, 0, 0, 0);
    }

    private ImageZoom mImageZoom;
    private WindowManager.LayoutParams mLayoutParamsWM;

    private int mPaddingLeft = 0;

}
