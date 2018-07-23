package com.vigrant.recapture;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class ImageCropper {

    ImageCropper(){
        setupLayout();
    }


    private void setupLayout(){
        mLayout = new LinearLayout(StaticMethods.mainActivity());
        StaticMethods.mainActivity().getLayoutInflater().inflate(R.layout.images, mLayout);

        TextView btnRotateRight = (TextView)mLayout.findViewById(R.id.images_btn_rotate_right);
        btnRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageZoom.rotateRight();
            }
        });

        TextView btnRotateLeft = (TextView)mLayout.findViewById(R.id.images_btn_rotate_left);
        btnRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageZoom.rotateLeft();
            }
        });

//        TextView btnZoom = (TextView)mLayout.findViewById(R.id.images_btn_zoom_out);
//        btnZoom.setText("zoom out");
//        btnZoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mImageZoom.zoomOut();
//            }
//        });




    }

//    void setImage(Bitmap bmp){
//        ((FrameLayout)mLayout.findViewById(R.id.images_layout_main)).removeAllViews();
//
//        mImageZoom = new ImageZoom(bmp);
//        ((FrameLayout)mLayout.findViewById(R.id.images_layout_main)).addView(mImageZoom);
//        mImageZoom.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mImageZoom.setOnTouchListener(new ImageTouchListener());
//
//    }

    LinearLayout layout(){
        return mLayout;
    }






    private ImageZoom mImageZoom;
    private LinearLayout mLayout;
    private float mZoom = 1.0f;
}
