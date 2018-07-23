package com.vigrant.recapture;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ImageZoom extends ImageView {

    public ImageZoom(Bitmap bmp){
        super(StaticMethods.mainActivity());
        mBitmap = bmp;
        mPaint.setAlpha(160);
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

//        void zoomOut(){
//            mZoom *= 0.9f;
//            mMatrix.postScale(mZoom, mZoom);
//            invalidate();
//        }

    private Paint mPaint = new Paint();
    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private float mZoom = 1.0f;
}