package com.vigrant.recapture;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.LinearLayout;

class DragLayout extends LinearLayout {

    private float mDownX, mDownY;
    private boolean mConsumeTouchEvents;
    static private float touchSlop = 5.0f;

    private Runnable mOnLeft, mOnRight;

    public DragLayout(Context context, Runnable onLeft, Runnable onRight) {
        super(context);
        mOnLeft = onLeft;
        mOnRight = onRight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isHandled = true;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float distanceY = mDownY - ev.getY();
                float distanceX = mDownX - ev.getX();
                if( !mConsumeTouchEvents) {
                    if( Math.abs( distanceY ) > Math.abs( distanceX ) ) {
                        if ( Math.abs(distanceY) > touchSlop) {

                            //send CANCEL event to the AbsListView so it doesn't scroll
                            //ev.setAction(MotionEvent.ACTION_CANCEL);
                            //isHandled = super.onTouchEvent(ev);
                            //mConsumeTouchEvents = true;
                            //if( 0.0f > distanceY ) 	mBoardAdapter.moveDown();
                            //else					mBoardAdapter.moveUp();
                        }
                    }
                    else {
                        if ( Math.abs(distanceX) > touchSlop) {
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            isHandled = super.onTouchEvent(ev);
                            //mConsumeTouchEvents = true;
                            mDownX = ev.getX();
                            if( 0.0f > distanceX ) 	mOnRight.run();
                            else					mOnLeft.run();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mConsumeTouchEvents = false;
                mDownY = ev.getY();
                mDownX = ev.getX();
                //fallthrough
            default:
                if (!mConsumeTouchEvents) {
                    isHandled = super.onTouchEvent(ev);
                }
                break;

        }
        return isHandled;
    }
}
