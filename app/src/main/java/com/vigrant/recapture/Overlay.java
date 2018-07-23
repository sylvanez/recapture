package com.vigrant.recapture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParamsButtons = new WindowManager.LayoutParams(displayMetrics.widthPixels, 150, 0, displayMetrics.heightPixels - 150, LAYOUT_TYPE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParamsButtons.gravity = Gravity.CENTER;

        LinearLayout layoutButtons = new LinearLayout(this);

        addButton("stop", new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }, layoutButtons);

        addButton("a++", new Runnable() {
            @Override
            public void run() {
                mImageZoom.alphaInc();
            }
        }, layoutButtons);

        addButton("a--", new Runnable() {
            @Override
            public void run() {
                mImageZoom.alphaDec();
            }
        }, layoutButtons);

        addButton("left", new Runnable() {
            @Override
            public void run() {
                left();
            }
        }, layoutButtons);

        addButton("right", new Runnable() {
            @Override
            public void run() {
                right();
            }
        }, layoutButtons);

        addButton("in", new Runnable() {
            @Override
            public void run() {
                mImageZoom.zoomIn();
            }
        }, layoutButtons);

        addButton("out", new Runnable() {
            @Override
            public void run() {
                mImageZoom.zoomOut();
            }
        }, layoutButtons);


        mWindowManager.addView(layoutButtons, layoutParamsButtons);

        StaticMethods.mainActivity().onOverlayStarted();
    }


    private void addButton(String text, final Runnable onClick, LinearLayout layout){
        Button buttonAlphaDec = new Button(this);
        buttonAlphaDec.setText(text);
        buttonAlphaDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.run();
            }
        });
        layout.addView(buttonAlphaDec);
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
