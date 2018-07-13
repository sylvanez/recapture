package com.vigrant.recapture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

        Button buttonStop = mLayout.findViewById(R.id.overlay_button_close);
        buttonStop.setText("close");

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.removeView(mLayout);
                stopSelf();
            }
        });

        Button buttonLoad = mLayout.findViewById(R.id.overlay_button_load);
        buttonLoad.setText("load image");

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticMethods.pickImage();
            }
        });

        WindowManager.LayoutParams layoutParamsWM = new WindowManager.LayoutParams(600, 400, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParamsWM.x = 0;
        layoutParamsWM.y = 0;
        layoutParamsWM.gravity = Gravity.CENTER;

        mWindowManager.addView(mLayout, layoutParamsWM);
    }

    void displayImage(Bitmap bitmap){
        ((ImageView)mLayout.findViewById(R.id.overlay_image)).setImageBitmap(bitmap);
        System.out.println("image of size " + bitmap.getWidth() + "x" + bitmap.getHeight() + " set");
        mLayout.invalidate();
    }


}
