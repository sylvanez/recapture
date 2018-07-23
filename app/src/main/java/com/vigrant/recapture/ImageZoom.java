package com.vigrant.recapture;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
class ImageZoom extends ImageView {

    public ImageZoom(Bitmap bmp){
        super(StaticMethods.mainActivity());
        mBitmap = bmp;
        mPaint.setAlpha(mAlpha);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    @Override
    public void setImageMatrix(Matrix m){
        mMatrix = m;
        invalidate();
        System.out.println(mMatrix.toShortString());
    }

    void rotateRight(){
        mMatrix.preRotate(90.0f, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
        invalidate();
    }

    void rotateLeft(){
        mMatrix.preRotate(-90.0f, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
        invalidate();
    }

    void alphaInc(){
        mAlpha += 10;
        if(mAlpha > 255) mAlpha = 255;
        mPaint.setAlpha(mAlpha);
        invalidate();
    }

    void alphaDec(){
        mAlpha -= 10;
        if(mAlpha < 0) mAlpha = 0;
        mPaint.setAlpha(mAlpha);
        invalidate();
    }

    void zoomOut(){
        mMatrix.postScale(0.99f, 0.99f);
        invalidate();
    }

    void zoomIn(){
        mMatrix.postScale(1.01f, 1.01f);
        invalidate();
    }
    private Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private float mZoom = 1.0f;
    private int mAlpha = 160;
}