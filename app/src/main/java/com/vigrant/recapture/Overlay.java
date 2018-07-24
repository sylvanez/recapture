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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

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
        mScaleDP = displayMetrics.density;
        WindowManager.LayoutParams layoutParamsButtons = new WindowManager.LayoutParams(displayMetrics.widthPixels, 150, 0, displayMetrics.heightPixels - 150, LAYOUT_TYPE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParamsButtons.gravity = Gravity.CENTER;

        LinearLayout layoutButtons = new LinearLayout(this);
        layoutButtons.setGravity(Gravity.CENTER);

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

        addButton("<<", new Runnable() {
            @Override
            public void run() {
                left();
            }
        }, layoutButtons);

        addButton(">>", new Runnable() {
            @Override
            public void run() {
                right();
            }
        }, layoutButtons);

        addButton("up", new Runnable() {
            @Override
            public void run() {
                up();
            }
        }, layoutButtons);

        addButton("dn", new Runnable() {
            @Override
            public void run() {
                down();
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

    int getPixelsForDP(int dp){
        return (int) (mScaleDP * (float)(dp) + 0.5f);
    }


    private void addButton(String text, final Runnable onClick, LinearLayout layout){
        TextView button = new TextView(this);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        button.setHeight(getPixelsForDP(28));
        button.setPadding(getPixelsForDP(8), getPixelsForDP(4), getPixelsForDP(8), getPixelsForDP(4));
        button.setText(text);
        button.setBackgroundColor(Color.rgb(120, 120, 120));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.run();
            }
        });

        layout.addView(button);

        Space sp = new Space(this);
        sp.setMinimumWidth(getPixelsForDP(8));
        sp.setMinimumHeight(getPixelsForDP(8));
        layout.addView(sp);
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

    private ImageZoom mImageZoom;
    private WindowManager.LayoutParams mLayoutParamsWM;
    private float mScaleDP = 1.0f;

    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;

}
