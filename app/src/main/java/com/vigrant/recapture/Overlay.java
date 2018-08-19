package com.vigrant.recapture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class Overlay extends Service {


    private WindowManager mWindowManager;
    private FrameLayout mLayout;

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

        mLayout = new FrameLayout(this);
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        //mScaleDP = displayMetrics.density;
        WindowManager.LayoutParams layoutParamsButtons = new WindowManager.LayoutParams(displayMetrics.widthPixels / 2, 150, 0, displayMetrics.heightPixels - 150, LAYOUT_TYPE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        layoutParamsButtons.gravity = Gravity.CENTER;

        DragLayout dragLayout = new DragLayout(this, new Runnable() {
            @Override
            public void run() {
                if (null != mImageZoom) mImageZoom.alphaDec();
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (null != mImageZoom) mImageZoom.alphaInc();
            }
        });
        dragLayout.setGravity(Gravity.CENTER);
        dragLayout.setBackgroundColor(Color.rgb(255, 255, 255));
        dragLayout.setAlpha(0.3f);

        mWindowManager.addView(dragLayout, layoutParamsButtons);

        StaticMethods.mainActivity().onOverlayStarted();
    }



    void displayImage(Bitmap bitmap){
        //((ImageView)mLayout.findViewById(R.id.overlay_image)).setImageBitmap(bitmap);
        if(null != mImageZoom){
            ((FrameLayout)mLayout.findViewById(R.id.overlay_layout_main)).removeView(mImageZoom);
        }

        mImageZoom = new ImageZoom(bitmap);
        ((FrameLayout)mLayout.findViewById(R.id.overlay_layout_main)).addView(mImageZoom);

        mLayout.invalidate();
    }

    void stop(){
        mWindowManager.removeView(mLayout);
        stopSelf();
    }

    void left(){
        mPaddingLeft -= 10;
        mLayout.setPadding(mPaddingLeft, mPaddingTop, 0, 0);
    }

    void right(){
        mPaddingLeft += 10;
        mLayout.setPadding(mPaddingLeft, mPaddingTop, 0, 0);
    }

    void up(){
        mPaddingTop  -= 10;
        mLayout.setPadding(mPaddingLeft, mPaddingTop, 0, 0);
    }

    void down(){
        mPaddingTop += 10;
        mLayout.setPadding(mPaddingLeft, mPaddingTop, 0, 0);
    }

    void zoomIn(){
        mImageZoom.zoomIn();
    }

    void zoomOut(){
        mImageZoom.zoomOut();
    }

    ImageZoom mImageZoom;
    private WindowManager.LayoutParams mLayoutParamsWM;

    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;

}
